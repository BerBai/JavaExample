package com.ber.netty.config;

//import com.ber.netty.handler.NettyClientHandler;
import com.ber.netty.handler.NettyServerFixedLengthHandler;
import com.ber.netty.handler.NettyServerHandler;
import com.ber.netty.handler.NettyServerLenghtFieldBasedHandler;
import com.ber.netty.handler.NettyServerLineBasedHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
                // 指定分割符处理器 1
                // .childHandler(new NettyServerHandler());
                // 指定为固定长度字节的处理器 2
                // .childHandler(new NettyServerFixedLengthHandler());
                // 请求头包含数据长度 3
                // .childHandler(new NettyServerLenghtFieldBasedHandler());
                // 通过换行符处理沾包/拆包 4
                .childHandler(new NettyServerLineBasedHandler());
        return serverBootstrap;
    }

    /**
     * 客户端启动器
     *
     * @return
     */
//    @Bean
//    public Bootstrap bootstrap() {
//        // 新建一组线程池
//        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(nettyProperties.getBoss());
//        Bootstrap bootstrap = new Bootstrap()
//                // 指定线程组
//                .group(eventExecutors)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                // 指定通道
//                .channel(NioSocketChannel.class)
//                // 指定处理器
//                .handler(new NettyClientHandler());
//        return bootstrap;
//    }

}
