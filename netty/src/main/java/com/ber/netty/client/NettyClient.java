package com.ber.netty.client;

import com.ber.netty.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author 鳄鱼儿
 * @Description 客户端
 * @date 2022/11/23 16:45
 * @Version 1.0
 */
public class NettyClient {
    private static Logger log = LoggerFactory.getLogger(NettyClient.class);
    Bootstrap bootstrap = null;
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(final String host, final int port) throws Exception {
        ChannelFuture future = null;
        try {
            if (bootstrap == null) {
                bootstrap = new Bootstrap();
            }
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    //TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildFixedLengthChannelHandler());
                    //.handler(new ChildDelimiterChannelHandler());
//                    .handler(new ChildLengthFieldChannelHandler());

            future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
            //加一个监听器，断线重连
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务端成功");
                    } else {
                        System.out.println("每隔2s重连....");
                        channelFuture.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    connect(host, port);
                                } catch (Exception e) {
                                    log.error("重连报错:{}", e.getMessage(), e);
                                }
                            }
                        }, 2, TimeUnit.SECONDS);
                    }
                }
            });
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            new NettyClient().connect("127.0.0.1", 18023);
        } catch (Exception e) {
            log.error("连接服务器异常:{}", e.getMessage(), e);
        }
    }

    /**
     * 固定长度
     *
     * @author Administrator
     */
    private class ChildFixedLengthChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 对服务端发送的消息进行粘包和拆包处理，由于服务端发送的消息已经进行了空格补全，
            // 并且长度为100，因而这里指定的长度也为100
            ch.pipeline().addLast(new FixedLengthFrameDecoder(100));
            // 将粘包和拆包处理得到的消息转换为字符串
            ch.pipeline().addLast(new StringDecoder());
            // 对客户端发送的消息进行空格补全，保证其长度为100
            ch.pipeline().addLast(new MessageEncodeFixedLengthHandler(100));
            // 客户端发送消息给服务端，并且处理服务端响应的消息
            ch.pipeline().addLast(new NettyClientHandler());
        }
    }

    private class ChildDelimiterChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            String delimiter = "##@##";
            //对服务端返回的消息通过_$进行分隔，并且每次查找的最大大小为1024字节
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.wrappedBuffer(delimiter.getBytes())));
            // 将前一步解码的数据转码为字符串
            ch.pipeline().addLast(new StringDecoder());
            //ch.pipeline().addLast(new StringEncoder());
            // 对客户端发送的数据进行编码，这里主要是在客户端发送的数据最后添加分隔符
            ch.pipeline().addLast(new MessageEncodeHandler(delimiter));
            // 最终的数据处理
            ch.pipeline().addLast(new NettyClientHandler());

        }
    }

    /**
     * 理粘拆包的主要思想是在生成的数据包中添加一个长度字段,用于记录当前数据包的长度,LengthFieldBasedFrameDecoder会按照参数指定的包长度偏移量数据对接收到的数据进行解码，从而得到目标消息体数据；而LengthFieldPrepender则会在响应的数据前面添加指定的字节数据，这个字节数据中保存了当前消息体的整体字节数据长度。
     *
     * @author zhouzhiyao
     */
    private class ChildLengthFieldChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 这里将LengthFieldBasedFrameDecoder添加到pipeline的首位，因为其需要对接收到的数据
            // 进行长度字段解码，这里也会对数据进行粘包和拆包处理
		  		/*
		  		 * 	maxFrameLength：指定了每个包所能传递的最大数据包大小；
					lengthFieldOffset：指定了长度字段在字节码中的偏移量；
					lengthFieldLength：指定了长度字段所占用的字节长度；
					lengthAdjustment：对一些不仅包含有消息头和消息体的数据进行消息头的长度的调整，这样就可以只得到消息体的数据，这里的lengthAdjustment指定的就是消息头的长度；
					initialBytesToStrip：对于长度字段在消息头中间的情况，可以通过initialBytesToStrip忽略掉消息头以及长度字段占用的字节。
		  		 */
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
            // LengthFieldPrepender是一个编码器，主要是在响应字节数据前面添加字节长度字段
            ch.pipeline().addLast(new LengthFieldPrepender(2));
            // 对经过粘包和拆包处理之后的数据进行json反序列化，从而得到UserInfo对象
            ch.pipeline().addLast(new MessageDecodeHandler());
            // 对响应数据进行编码，主要是将UserInfo对象序列化为json
            ch.pipeline().addLast(new MessageDecodeHandler());
            // 最终的数据处理
            ch.pipeline().addLast(new NettyClientHandler());

        }
    }
}
