package com.channel.Channel.Service.Impl;

import com.channel.Channel.Service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public void handleMessage(WebSocketSession session, TextMessage message) throws Exception {
       
    }
}
