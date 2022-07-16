package vn.tpsc.it4u.websocket;

import vn.tpsc.it4u.constants.ConstantPool;
import vn.tpsc.it4u.services.WebSSHService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WebSSHWebSocketHandler implements WebSocketHandler {
	@Autowired
	private WebSSHService webSSHService;

	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		webSSHService.initConnection(webSocketSession);
	}

	@Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage)
			throws Exception {
		if (webSocketMessage instanceof TextMessage) {
			webSSHService.recvHandle(((TextMessage) webSocketMessage).getPayload(), webSocketSession);
		} else if (webSocketMessage instanceof BinaryMessage) {

		} else if (webSocketMessage instanceof PongMessage) {

		} else {
			System.out.println("Unexpected WebSocket message type: " + webSocketMessage);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
		log.error("Error");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		webSSHService.close(webSocketSession);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
