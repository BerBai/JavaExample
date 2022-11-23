package com.ber.netty.handler;

import com.ber.netty.domain.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @Author 鳄鱼儿
 * @Description 消息解码
 * @date 2022/11/22 20:25
 * @Version 1.0
 */

public class MessageDecodeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        ByteBuf frame = in.retainedDuplicate();
        final String content = frame.toString(CharsetUtil.UTF_8);
        Message message = new Message(content);
        list.add(message);
        in.skipBytes(in.readableBytes());
    }
}
