package com.channel.Channel.Config;

import com.channel.Channel.Handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@CrossOrigin
public class MessageConfig implements WebSocketConfigurer {

    private final MessageHandler messageHandler;

    @Autowired
    public MessageConfig(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageHandler, "/message")
//                .setAllowedOrigins("https://channel-messaging.netlify.app")
                .setAllowedOrigins("http://192.168.1.21:3000")
//                .setAllowedOrigins("*")
        ;
    }
}
