package com.channel.Channel.Service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageService {
    public void handleMessage(WebSocketSession session, TextMessage message) throws Exception;
}
