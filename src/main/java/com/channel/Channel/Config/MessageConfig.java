package com.channel.Channel.Config;

import com.channel.Channel.Handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MessageConfig implements WebSocketConfigurer {

    private final MessageHandler messageHandler;

    @Autowired
    public MessageConfig(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler, "/message").setAllowedOrigins("*");
    }
}
