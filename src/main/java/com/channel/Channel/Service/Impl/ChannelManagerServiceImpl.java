package com.channel.Channel.Service.Impl;

import com.channel.Channel.Model.Channel;
import com.channel.Channel.Service.ChannelManagerService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Data
public class ChannelManagerServiceImpl implements ChannelManagerService {
    // Map to store channels by their name
    private final Map<String, Channel> channels = new ConcurrentHashMap<>();

    // Retrieve an existing channel or create a new one if it doesn't exist
    public Channel getOrCreateChannel(String channelName) {
        return channels.computeIfAbsent(channelName, name -> {
            Channel newChannel = new Channel();
            newChannel.setChannelName(name);
            return newChannel;
        });
    }

    // Remove a channel if it becomes empty
    public void removeChannelIfEmpty(String channelName) {
        channels.computeIfPresent(channelName, (name, channel) -> {
            if (channel.getChannelUsers().isEmpty() && channel.getSessions().isEmpty()) {
                return null; // Remove the channel
            }
            return channel;
        });
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }
}
