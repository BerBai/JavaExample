package com.ber.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * @Author 鳄鱼儿
 * @Description 采用固定长度字节处理沾包拆包问题
 * @date 2022/11/23 15:50
 * @Version 1.0
 */

public class NettyServerFixedLengthHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 固定字节长度
        Integer length = 100;
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 按固定100字节数拆分接收到的ByteBuf的解码器
        pipeline.addLast(new FixedLengthFrameDecoder(length));
        // 将上一步解码后的数据转码为Message实例
        pipeline.addLast(new MessageDecodeHandler());
        // 对发送客户端的数据进行编码，字节长度不足补0
        pipeline.addLast(new MessageEncodeFixedLengthHandler(length));
        // 对数据进行最终处理
        pipeline.addLast(new ServerListenerHandler());
    }
}
