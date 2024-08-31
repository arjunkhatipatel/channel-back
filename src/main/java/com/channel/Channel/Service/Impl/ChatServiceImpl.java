package com.channel.Channel.Service.Impl;

import com.channel.Channel.DTO.ChatDto;
import com.channel.Channel.DTO.ResponseDto;
import com.channel.Channel.Model.Channel;
import com.channel.Channel.Service.ChatService;
import com.channel.Channel.Utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    ChannelManagerServiceImpl channelManagerService;

    public ResponseEntity<ResponseDto> loginUser(ChatDto chatDto) {

        Channel channel = channelManagerService.getOrCreateChannel(chatDto.getChannel());

        System.out.println("Channel: " + channel);

        // Check if a user with the same name already exists in the channel
        if (channel.getChannelUsers().contains(chatDto.getUsername())) {
            return responseUtil.createResponse(null, HttpStatus.CONFLICT, 409, "Username already taken, please choose another.");
        }

        // Check if the channel already exists and the password matches
        if (channel.getPassword() != null && !channel.getPassword().equals(chatDto.getPassword())) {
            return responseUtil.createResponse(null, HttpStatus.UNAUTHORIZED, 401, "Incorrect password.");
        }

        // Store user details in channel
        channel.getChannelUsers().add(chatDto.getUsername());
        channel.setPassword(chatDto.getPassword());

        // Generate a session token or unique ID for the user
        String sessionId = UUID.randomUUID().toString();

        // Store user details in the channel or session store
        channel.addUser(chatDto.getUsername(), sessionId);

        System.out.println("Channel Users: " + channel);

        // You can return this token/id to the frontend so it can be used later in WebSocket connections
        return responseUtil.createResponse(sessionId, HttpStatus.OK, 200, "Logged in Successfully");
    }
}
