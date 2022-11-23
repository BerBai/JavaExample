package com.ber.netty.client;

import com.ber.netty.config.NettyProperties;
import com.ber.netty.domain.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author 鳄鱼儿
 * @Description 客户端启动器
 * @date 2022/11/23 08:59
 * @Version 1.0
 */

//@Component
//public class NettyClientBoot {
//    @Autowired
//    private Bootstrap bootstrap;
//
//    @Autowired
//    private NettyProperties nettyProperties;
//
//    /**
//     * 连接服务端主端口
//     *
//     * @return
//     * @throws InterruptedException
//     */
//    public Channel connect() throws InterruptedException {
//        // 连接服务器
//        ChannelFuture channelFuture = bootstrap.connect(nettyProperties.getHost(), nettyProperties.getPort()).sync();
//        // 监听关闭
//        Channel channel = channelFuture.channel();
//        return channel;
//    }
//
//    /**
//     * 连接服务端备用端口
//     *
//     * @return
//     * @throws InterruptedException
//     */
//    public Channel connectSlave() throws InterruptedException {
//        // 连接服务器
//        ChannelFuture channelFuture = bootstrap.connect(nettyProperties.getHost(), nettyProperties.getPort()).sync();
//        // 监听关闭
//        Channel channel = channelFuture.channel();
//        channel.closeFuture().sync();
//        return channel;
//    }
//
//    /**
//     * 发送消息至服务端
//     *
//     * @param message
//     * @throws InterruptedException
//     */
//    public void sendMsg(Message message) throws InterruptedException {
//        connect().writeAndFlush(message);
//    }
//}
