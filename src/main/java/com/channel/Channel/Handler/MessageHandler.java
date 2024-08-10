package com.channel.Channel.Handler;

import com.channel.Channel.DTO.IncomingMessage;
import com.channel.Channel.Model.Channel;
import com.channel.Channel.Service.Impl.ChannelManagerServiceImpl;
import com.channel.Channel.Service.Impl.MessageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Component
public class MessageHandler extends TextWebSocketHandler {
    private final MessageServiceImpl messageService;
    private final ChannelManagerServiceImpl channelManagerService;

    Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    public MessageHandler(MessageServiceImpl messageService, ChannelManagerServiceImpl channelManagerService) {
        this.messageService = messageService;
        this.channelManagerService = channelManagerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Generate a unique identifier for the session
        String uniqueId = UUID.randomUUID().toString();
        session.getAttributes().put("userId", uniqueId);

        logger.info("Connection established: " + session.getId() + ", User ID: " + uniqueId);
        session.sendMessage(new TextMessage("Welcome Abroad!"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Assuming the payload is a JSON string containing channel and name
        String payload = message.getPayload();
        logger.info("Received message: " + payload);

        // Parse the JSON payload to extract the room (channel) name and username
        ObjectMapper objectMapper = new ObjectMapper();
        IncomingMessage incomingMessage = objectMapper.readValue(payload, IncomingMessage.class);

        String msgType = incomingMessage.getType();
        String channelName = incomingMessage.getChannel();
        String userName = incomingMessage.getName();
        String content = incomingMessage.getMsg();

        // Get or create the Channel instance for the room
        Channel channel = channelManagerService.getOrCreateChannel(channelName);

        channel.addSession(session);

        // Store the channel name and username in the session attributes
        session.getAttributes().put("channelName", channelName);
        session.getAttributes().put("userName", userName);
        // Send message to newly joined user and all the members in the room
        if (msgType.equals("login")) {
            // Add the user to the channel
            channel.getChannelUsers().add(userName);

            //Sending old message to new user
            for (String oldMessage : channel.getChannelMsg()) {
                session.sendMessage(new TextMessage(oldMessage));
            }

            String userListMessage = "Users in " + channel.getChannelName() + ": " + String.join(", ", channel.getChannelUsers());
            session.sendMessage(new TextMessage(userListMessage));


            // Notify all other users in the channel that a new user has joined
            String joinNotification = userName + " has joined the channel.";
            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                if (!wsSession.equals(session) && channel.getChannelUsers().contains(userName)) {
                    wsSession.sendMessage(new TextMessage(joinNotification));
                }
            }
        }

        if (msgType.equals("msg") && !content.isEmpty()) {
            String broadcastMessage = userName + " : " + content;

            // Store the new message in the channel
            channel.setMsg(broadcastMessage);

            // Broadcast the message to all users in the channel
            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                wsSession.sendMessage(new TextMessage(broadcastMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Retrieve the channel name from session attributes
        String channelName = (String) session.getAttributes().get("channelName");
        String userName = (String) session.getAttributes().get("userName");

        if (channelName != null && userName != null) {
            Channel channel = channelManagerService.getOrCreateChannel(channelName);

            channel.getChannelUsers().remove(userName);
            channel.removeSession(session);

            // Notify remaining users
            String leaveNotification = userName + " has left the channel.";
            for (WebSocketSession wsSession : channel.getOpenSessions()) {
                wsSession.sendMessage(new TextMessage(leaveNotification));
            }

            logger.info("User '" + userName + "' disconnected from channel '" + channelName + "'");

            // Remove the channel if it's empty
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
