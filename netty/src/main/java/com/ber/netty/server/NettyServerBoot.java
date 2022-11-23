package com.ber.netty.server;

import com.ber.netty.config.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author 鳄鱼儿
 * @Description Metty启动/关闭
 * @date 2022/11/22 16:39
 * @Version 1.0
 */
@Component
public class NettyServerBoot {
    private static final Logger log = LoggerFactory.getLogger(NettyServerBoot.class);
    @Resource
    NioEventLoopGroup boosGroup;
    @Resource
    NioEventLoopGroup workerGroup;
    final ServerBootstrap serverBootstrap;
    final NettyProperties nettyProperties;

    public NettyServerBoot(ServerBootstrap serverBootstrap, NettyProperties nettyProperties) {
        this.serverBootstrap = serverBootstrap;
        this.nettyProperties = nettyProperties;
    }


    /**
     * 启动netty
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 绑定端口启动
        serverBootstrap.bind(nettyProperties.getPort()).sync();
        // 备用端口
        serverBootstrap.bind(nettyProperties.getPortSalve()).sync();
        log.info("启动Netty: {},{}", nettyProperties.getPort(), nettyProperties.getPortSalve());
    }

    /**
     * 关闭netty
     */
    @PreDestroy
    public void close() {
        log.info("关闭Netty");
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
