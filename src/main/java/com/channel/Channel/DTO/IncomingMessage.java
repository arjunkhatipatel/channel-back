package com.channel.Channel.DTO;

import lombok.Data;

@Data
public class IncomingMessage {
    private String type;
    private String channel;
    private String name;
    private String msg;

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "type='" + type + '\'' +
                ", channel='" + channel + '\'' +
                ", name='" + name + '\'' +
                ", content='" + msg + '\'' +
                '}';
    }
}
