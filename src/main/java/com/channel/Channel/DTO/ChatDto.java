package com.channel.Channel.DTO;

import lombok.Data;

@Data
public class ChatDto {
    private String type;
    private String channel;
    private String password;
    private String username;
    private String msg;
    private String sessionId;

    @Override
    public String toString() {
        return "ChatDto{" +
                "type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", password='" + password + '\'' +
                ", name='" + username + '\'' +
                ", msg='" + msg + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
