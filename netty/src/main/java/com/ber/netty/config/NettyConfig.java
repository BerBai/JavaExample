package com.ber.netty.config;

import com.ber.netty.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 鳄鱼儿
 * @Description netty配置
 * @date 2022/11/22 16:32
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties
public class NettyConfig {
    final NettyProperties nettyProperties;

    public NettyConfig(NettyProperties nettyProperties) {
        this.nettyProperties = nettyProperties;
    }

    /**
     * boss线程池-进行客户端连接
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup boosGroup() {
        return new NioEventLoopGroup(nettyProperties.getBoss());
    }

    /**
     * worker线程池-进行业务处理
     *
     * @return
     */
    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorker());
    }

    /**
     * 服务启动器
     *
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // 指定使用的线程组
                .group(boosGroup(), workerGroup())
                // 指定使用的通道
                .channel(NioServerSocketChannel.class)
                // 指定连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyProperties.getTimeout())
                // 指定worker处理器
                .childHandler(new NettyServerHandler());
        return serverBootstrap;
    }

}
