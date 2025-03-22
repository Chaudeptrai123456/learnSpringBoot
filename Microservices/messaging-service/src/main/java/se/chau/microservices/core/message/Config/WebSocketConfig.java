package se.chau.microservices.core.message.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.socket.server.support.WebSocketHandlerMapping;
import se.chau.microservices.core.message.Service.ChatWebSocketHandler;

@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketHandlerMapping webSocketHandlerMapping(ChatWebSocketHandler chatWebSocketHandler) {
        return new WebSocketHandlerMapping();
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}