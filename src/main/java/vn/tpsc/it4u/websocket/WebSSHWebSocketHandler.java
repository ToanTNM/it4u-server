package vn.tpsc.it4u.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;
import vn.tpsc.it4u.services.WebSSHService;

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
