package com.ber.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @Author 鳄鱼儿
 * @Description 初始化netty
 * @date 2022/11/22 20:17
 * @Version 1.0
 */

public class NettyServerLineBasedHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 使用LineBasedFrameDecoder解决粘包问题，其会根据"\n"或"\r\n"对二进制数据进行拆分，封装到不同的ByteBuf实例中，并且每次查找的最大长度为1024字节
        pipeline.addLast(new LineBasedFrameDecoder(1024, true, true));
        // 将上一步解码后的数据转码为Message实例
        pipeline.addLast(new MessageDecodeHandler());
        // 对发送客户端的数据进行编码
        pipeline.addLast(new MessageEncodeHandler());
        // 对数据进行最终处理
        pipeline.addLast(new ServerListenerHandler());
    }
}
