package com.ber.netty.handler;

import com.ber.netty.domain.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author 鳄鱼儿
 * @Description 使用固定字节长度编码消息，字节长度不足时补0
 * @date 2022/11/23 16:06
 * @Version 1.0
 */

public class MessageEncodeFixedLengthHandler extends MessageToByteEncoder<Message> {
    private int length;

    public MessageEncodeFixedLengthHandler(int length) {
        this.length = length;
    }


    /**
     * 使用固定字节长度编码消息，字节长度不足时补0
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        String jsonStr = msg.toJsonString();
        // 如果长度不足，则进行补0
        if (jsonStr.length() < length) {
            jsonStr = addSpace(jsonStr);
        }
        // 使用Unpooled.wrappedBuffer实现零拷贝，将字符串转为ByteBuf
        ctx.writeAndFlush(Unpooled.wrappedBuffer(jsonStr.getBytes()));
    }

    /**
     * 如果没有达到指定长度进行补0
     *
     * @param msg
     * @return
     */
    private String addSpace(String msg) {
        StringBuilder builder = new StringBuilder(msg);
        for (int i = 0; i < length - msg.length(); i++) {
            builder.append(0);
        }
        return builder.toString();
    }

}
