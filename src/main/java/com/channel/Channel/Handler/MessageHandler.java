package com.channel.Channel.Handler;

import com.channel.Channel.DTO.ChatDto;
import com.channel.Channel.Model.Channel;
import com.channel.Channel.Service.Impl.ChannelManagerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MessageHandler extends TextWebSocketHandler {
    @Autowired
    ChannelManagerServiceImpl channelManagerService;

    @Autowired
    Channel channel;

    Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established: " + session.getId());
    }


    private boolean isValidSession(String sessionId) {
        return sessionId != null && getUserNameFromSessionId(sessionId) != null;
    }

    private String getUserNameFromSessionId(String sessionId) {
        for (Channel channel : channelManagerService.getAllChannels()) {
            String userName = channel.getUserBySessionId(sessionId);
            if (userName != null) {
                return userName;
            }
        }
        return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the JSON payload
        String payload = message.getPayload();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ChatDto chatDto = objectMapper.readValue(payload, ChatDto.class);

        String msgType = chatDto.getType();
        String channelName = chatDto.getChannel();
        String userName = chatDto.getUsername();
        String password = chatDto.getPassword();
        String content = chatDto.getMsg();
        String sessionId = chatDto.getSessionId();

        // Get or create the Channel instance for the room
        Channel channel = channelManagerService.getOrCreateChannel(channelName);

        // Store the channel name and username in the session attributes
        session.getAttributes().put("channelName", channelName);
        session.getAttributes().put("userName", userName);

        if ("first".equals(msgType)) {
            session.getAttributes().put("userId", sessionId);

            // Check if username is already taken
            if (!channel.userExists(userName)) {
                session.sendMessage(new TextMessage("{\"error\":\"Username mot found.\"}"));
                return;
            }

            channel.addUser(userName, sessionId);
            channel.addSession(session);

            // Send previous messages to the new user
//            for (String oldMessage : channel.getChannelMsg()) {
//                session.sendMessage(new TextMessage("{\"message\":\"" + oldMessage + "\"}"));
//            }

            // Send user list to the new user
            String userListMessage = "{\"userList\":\"Users in " + channel.getChannelName() + ": " + String.join(", ", channel.getChannelUsers()) + "\"}";
            session.sendMessage(new TextMessage(userListMessage));

            // Notify all users of the new user
            String joinNotification = "{\"notification\":\"" + userName + " has joined the channel.\"}";

            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                if (!wsSession.equals(session) && channel.getChannelUsers().contains(userName)) {
                    wsSession.sendMessage(new TextMessage(joinNotification));
                }
            }

        } else if ("msg".equals(msgType) && content != null && !content.isEmpty()) {
            // Broadcast message to all users in the channel
            String broadcastMessage = "{\"message\":\"" + userName + " : " + content + "\"}";
            channel.setMsg(broadcastMessage);
            // Broadcast the message to all users in the channel
            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                wsSession.sendMessage(new TextMessage(broadcastMessage));
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String channelName = (String) session.getAttributes().get("channelName");
        String userName = (String) session.getAttributes().get("userName");

        if (channelName != null && userName != null) {
            Channel channel = channelManagerService.getOrCreateChannel(channelName);

            channel.getChannelUsers().remove(userName);
            channel.removeSession(session);

            String leaveNotification = "{\"notification\":\"" + userName + " has left the channel.\"}";

            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                wsSession.sendMessage(new TextMessage(leaveNotification));
            }

            logger.info("User '" + userName + "' disconnected from channel '" + channelName + "'");

            channelManagerService.removeChannelIfEmpty(channelName);
        }
        logger.info("Connection closed: " + session.getId() + ", Status: " + status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Error occurred: " + exception.getMessage());
        session.close();
    }
}
