package com.doodl6.mq.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.doodl6.mq.helper.Common.*;
@ChannelHandler.Sharable
public class ZhangChatHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//            System.out.println(msg);
        System.out.println("ZhangChatHandler");
        if (L1.equals(msg)) {
            //啥也不做，等下一条消息
        } else if (L2.equals(msg)) {
            ctx.channel().writeAndFlush(Z3);
        } else if (L4.equals(msg)) {
            ctx.channel().writeAndFlush(Z5);
        } else {
            System.out.println("Zhang异常:" + msg);
        }
    }

}
