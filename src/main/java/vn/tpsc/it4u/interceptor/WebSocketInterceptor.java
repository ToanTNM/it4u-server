package vn.tpsc.it4u.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import vn.tpsc.it4u.constants.ConstantPool;

import java.util.Map;
import java.util.UUID;

public class WebSocketInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
			WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
		if (serverHttpRequest instanceof ServletServerHttpRequest) {
			// ServletServerHttpRequest request = (ServletServerHttpRequest)
			// serverHttpRequest;
			String uuid = UUID.randomUUID().toString().replace("-", "");
			map.put(ConstantPool.USER_UUID_KEY, uuid);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
			WebSocketHandler webSocketHandler, Exception e) {

	}
}
