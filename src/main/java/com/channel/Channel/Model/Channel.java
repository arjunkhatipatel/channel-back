package com.channel.Channel.Model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;


@Component
@Data
public class Channel {
    private String channelName;
    private String password;
    private Set<String> channelUsers = new HashSet<>();
    private List<String> channelMsg = new ArrayList<>();
    private Set<WebSocketSession> sessions = new HashSet<>();
    // Map to store UUID to userName
    private Map<String, String> sessionUserMap = new HashMap<>();


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

    public String getUserBySessionId(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    public void removeUser(String sessionId) {
        String userName = sessionUserMap.remove(sessionId);
        channelUsers.remove(userName);
    }

    // Add user to the channel and map their session ID (UUID) to their username
    public void addUser(String userName, String sessionId) {
        channelUsers.add(userName);
        sessionUserMap.put(sessionId, userName);
    }

    public boolean userExists(String userName) {
        return channelUsers.contains(userName);
    }

}
