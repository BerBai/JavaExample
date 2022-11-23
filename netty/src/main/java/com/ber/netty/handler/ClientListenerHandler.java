package com.ber.netty.handler;

import com.ber.netty.domain.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author 鳄鱼儿
 * @Description 客户端业务
 * @date 2022/11/23 08:46
 * @Version 1.0
 */

public class ClientListenerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger log = LoggerFactory.getLogger(ClientListenerHandler.class);

    /**
     * 连接服务端
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("{}连上了服务器", ctx.channel().remoteAddress());
    }

    /**
     * 断开服务端
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("{}断开了服务器", ctx.channel().remoteAddress());
        ctx.fireChannelInactive();
    }


    /**
     * 收到服务端消息
     *
     * @param channelHandlerContext
     * @param message
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        log.info("收到服务端的消息:{}", message.getContent());
        channelHandlerContext.channel().close();
    }

    /**
     * 异常发生时候调用
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}连接发生异常", ctx.channel().remoteAddress());
        log.error(cause.getCause().toString());
        ctx.close();
    }
}