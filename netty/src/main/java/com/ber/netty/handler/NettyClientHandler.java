package com.ber.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * @Author 鳄鱼儿
 * @Description TODO
 * @date 2022/11/23 08:54
 * @Version 1.0
 */

//public class NettyClientHandler extends ChannelInitializer<SocketChannel> {
//    @Override
//    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        // 数据分割符
//        String delimiterStr = "##@##";
//        ByteBuf delimiter = Unpooled.copiedBuffer(delimiterStr.getBytes());
//        ChannelPipeline pipeline = socketChannel.pipeline();
//        // 使用自定义处理拆包/沾包，并且每次查找的最大长度为1024字节
//        pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
//        pipeline.addLast(new MessageEncodeHandler(delimiterStr));
//        pipeline.addLast(new MessageDecodeHandler());
//        pipeline.addLast(new ClientListenerHandler());
//    }
//}
