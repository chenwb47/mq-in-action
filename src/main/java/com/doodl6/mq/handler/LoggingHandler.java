package com.doodl6.mq.handler;

import com.doodl6.mq.helper.Common;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
@ChannelHandler.Sharable
public class LoggingHandler extends SimpleChannelInboundHandler<String> {

    private String name;
    private int msgGroupCount;
    private AtomicInteger count = new AtomicInteger(0);


    public LoggingHandler(String name, int msgGroupCount) {
        this.name = name;
        this.msgGroupCount = msgGroupCount;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {

        if (count.incrementAndGet() == Common.MEET_COUNT * this.msgGroupCount) {
            System.out.println(this.name + " 处理完成。");
        }
        //System.out.println(msg);
        System.out.println(this.name + "logger");
        ctx.fireChannelRead(msg);
    }
}
