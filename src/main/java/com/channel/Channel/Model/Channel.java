package com.channel.Channel.Model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@Data
public class Channel {
    private String channelName;
    private String password;
    private List<String> channelUsers = new ArrayList<>();
    private List<String> channelMsg = new ArrayList<>();
    private Set<WebSocketSession> sessions = new HashSet<>();

    public void setMsg(String msg) {
        channelMsg.add(msg);
    }

    // Method to retrieve all messages in the channel
    public List<String> getChannelMsg() {
        return new ArrayList<>(channelMsg);
    }
    
    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public WebSocketSession[] getOpenSessions() {
        return sessions.toArray(new WebSocketSession[0]);
    }

    public WebSocketSession[] getAllMessages() {
        return channelMsg.toArray(new WebSocketSession[0]);
    }
}
