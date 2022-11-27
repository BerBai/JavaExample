package com.ber.netty.service.impl;

import com.ber.netty.domain.Message;
import com.ber.netty.server.ChannelMap;
import com.ber.netty.service.PushMsgService;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * @Author 鳄鱼儿
 * @Description 向客户端发送消息
 * @date 2022/11/27 17:29
 * @Version 1.0
 */

@Service
public class PushMsgServiceImpl implements PushMsgService {

    /**
     * 向一个客户端发送消息
     *
     * @param msg
     */
    @Override
    public void push(Message msg) {
        // 客户端ID
        String id = msg.getId();
        Channel channel = ChannelMap.getChannel(id);
        if (null == channel) {
            throw new RuntimeException("客户端已离线");
        }
        channel.writeAndFlush(msg.toJsonString());
    }
}
