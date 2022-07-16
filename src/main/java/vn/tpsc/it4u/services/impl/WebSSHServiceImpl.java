
package vn.tpsc.it4u.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import vn.tpsc.it4u.constants.ConstantPool;
import vn.tpsc.it4u.models.SSHConnectInfo;
import vn.tpsc.it4u.models.WebSSHData;
import vn.tpsc.it4u.services.WebSSHService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WebSSHServiceImpl implements WebSSHService {
	private static Map<String, Object> sshMap = new ConcurrentHashMap<>();

	private Logger logger = LoggerFactory.getLogger(WebSSHServiceImpl.class);
	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void initConnection(WebSocketSession session) {
		JSch jSch = new JSch();
		SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
		sshConnectInfo.setjSch(jSch);
		sshConnectInfo.setWebSocketSession(session);
		String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
		sshMap.put(uuid, sshConnectInfo);
	}

	@Override
	public void recvHandle(String buffer, WebSocketSession session) {
		ObjectMapper objectMapper = new ObjectMapper();
		WebSSHData webSSHData = null;
		try {
			webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
		} catch (IOException e) {
			return;
		}
		String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
		if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
			SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
			WebSSHData finalWebSSHData = webSSHData;
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						connectToSSH(sshConnectInfo, finalWebSSHData, session);
					} catch (JSchException | IOException e) {
						close(session);
					}
				}
			});
		} else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
			String command = webSSHData.getCommand();
			SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
			if (sshConnectInfo != null) {
				try {
					transToSSH(sshConnectInfo.getChannel(), command);
				} catch (IOException e) {
					close(session);
				}
			}
		} else {
			close(session);
		}
	}

	@Override
	public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
		session.sendMessage(new TextMessage(buffer));
	}

	@Override
	public void close(WebSocketSession session) {
		String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
		SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
		if (sshConnectInfo != null) {
			if (sshConnectInfo.getChannel() != null)
				sshConnectInfo.getChannel().disconnect();
			sshMap.remove(userId);
		}
	}

	private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession)
			throws JSchException, IOException {
		Session session = null;
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(),
				webSSHData.getPort());
		session.setConfig(config);
		session.setPassword(webSSHData.getPassword());
		session.connect(30000);

		Channel channel = session.openChannel("shell");

		channel.connect(3000);

		sshConnectInfo.setChannel(channel);

		transToSSH(channel, "\r");

		InputStream inputStream = channel.getInputStream();
		try {
			byte[] buffer = new byte[1024];
			int i = 0;
			while ((i = inputStream.read(buffer)) != -1) {
				sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
			}

		} finally {
			session.disconnect();
			channel.disconnect();
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}

	private void transToSSH(Channel channel, String command) throws IOException {
		if (channel != null) {
			OutputStream outputStream = channel.getOutputStream();
			outputStream.write(command.getBytes());
			outputStream.flush();
		}
	}
}
