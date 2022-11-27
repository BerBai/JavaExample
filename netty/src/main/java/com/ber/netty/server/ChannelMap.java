package com.ber.netty.server;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 鳄鱼儿
 * @Description 连接通道保存MAP
 * @date 2022/11/27 16:30
 * @Version 1.0
 */

public class ChannelMap {
    /**
     * 存放客户端标识ID（消息ID）与channel的对应关系
     */
    private static volatile ConcurrentHashMap<String, Channel> channelMap = null;

    private ChannelMap() {
    }

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        if (null == channelMap) {
            synchronized (ChannelMap.class) {
                if (null == channelMap) {
                    channelMap = new ConcurrentHashMap<>();
                }
            }
        }
        return channelMap;
    }

    public static Channel getChannel(String id) {
        return getChannelMap().get(id);
    }
}
