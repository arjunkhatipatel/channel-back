package com.channel.Channel.Service;

import com.channel.Channel.Model.Channel;

import java.util.Map;

public interface ChannelManagerService {
    public Channel getOrCreateChannel(String channelName);

    public void removeChannelIfEmpty(String channelName);

    public Map<String, Channel> getChannels();
}
