package vn.tpsc.it4u.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import vn.tpsc.it4u.interceptor.WebSocketInterceptor;
import vn.tpsc.it4u.websocket.WebSSHWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSSHWebSocketConfig implements WebSocketConfigurer {
	@Autowired
	WebSSHWebSocketHandler webSSHWebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

		webSocketHandlerRegistry.addHandler(webSSHWebSocketHandler, "/webssh")
				.addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
	}
}