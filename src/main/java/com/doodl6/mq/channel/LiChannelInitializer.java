package com.doodl6.mq.channel;

import com.doodl6.mq.handler.LiChatHandler;
import com.doodl6.mq.handler.LoggingHandler;
import com.doodl6.mq.helper.ChatDecoder;
import com.doodl6.mq.helper.ChatEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class LiChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ChatEncoder());
        pipeline.addLast(new ChatDecoder());
//        pipeline.addLast(new LoggingHandler("LEE",2));
        //聊天处理
        pipeline.addLast(new LiChatHandler());

    }
}
