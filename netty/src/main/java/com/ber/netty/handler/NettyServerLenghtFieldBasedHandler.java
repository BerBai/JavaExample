package com.ber.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Author 鳄鱼儿
 * @Description 采用固定长度字节处理沾包拆包问题
 * @date 2022/11/23 15:50
 * @Version 1.0
 */

public class NettyServerLenghtFieldBasedHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 请求头包含数据长度，根据长度进行沾包拆包处理
        /**
         * maxFrameLength：指定了每个包所能传递的最大数据包大小；
         * lengthFieldOffset：指定了长度字段在字节码中的偏移量；
         * lengthFieldLength：指定了长度字段所占用的字节长度；
         * lengthAdjustment：对一些不仅包含有消息头和消息体的数据进行消息头的长度的调整，这样就可以只得到消息体的数据，这里的lengthAdjustment指定的就是消息头的长度；
         * initialBytesToStrip：对于长度字段在消息头中间的情况，可以通过initialBytesToStrip忽略掉消息头以及长度字段占用的字节。
         */
        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
        // 在请求头添加字节长度字段
        pipeline.addLast(new LengthFieldPrepender(2));
        // 将上一步解码后的数据转码为Message实例
        pipeline.addLast(new MessageDecodeHandler());
        // 对发送客户端的数据进行编码
        pipeline.addLast(new MessageEncodeHandler());
        // 对数据进行最终处理
        pipeline.addLast(new ServerListenerHandler());
    }
}
