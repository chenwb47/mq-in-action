package com.doodl6.mq.handler;

import com.doodl6.mq.helper.Common;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class LiChatHandler extends SimpleChannelInboundHandler<String> {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//            System.out.println(msg);
        if (Common.Z0.equals(msg)) {
            ctx.channel().writeAndFlush(Common.L1);
            ctx.channel().writeAndFlush(Common.L2);
        } else if (Common.Z3.equals(msg)) {
            ctx.channel().writeAndFlush(Common.L4);
        } else if (Common.Z5.equals(msg)) {
            int total = count.incrementAndGet();
                // System.out.println(total);
            if (Common.MEET_COUNT == total) {
                ctx.channel().close();
            }
        } else {
            System.out.println("Li异常:" + msg);
        }
    }

}
