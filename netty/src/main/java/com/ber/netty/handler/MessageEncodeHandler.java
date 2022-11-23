package com.ber.netty.handler;

import com.ber.netty.domain.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Author 鳄鱼儿
 * @Description 消息编码
 * @date 2022/11/22 20:25
 * @Version 1.0
 */

public class MessageEncodeHandler extends MessageToByteEncoder<Message> {
    // 数据分割符
    String delimiter;

    public MessageEncodeHandler(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        out.writeBytes((message.toJsonString() + delimiter).getBytes(CharsetUtil.UTF_8));
    }
}
