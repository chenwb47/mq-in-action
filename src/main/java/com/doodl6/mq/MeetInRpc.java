package com.doodl6.mq;

import com.doodl6.mq.channel.LiChannelInitializer;
import com.doodl6.mq.channel.ZhangChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import static com.doodl6.mq.helper.Common.*;

public class MeetInRpc {


    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap liBootstrap = new ServerBootstrap();
            liBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(6666))
                    .childHandler(new LiChannelInitializer())
                    .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            ChannelFuture liChannelFuture = liBootstrap.bind().sync();
            Channel liChannel = liChannelFuture.channel();

            System.out.println("李大爷开始等待。。");

            new Thread(() -> {
                Bootstrap zhangBootstrap = new Bootstrap();
                EventLoopGroup clientGroup = new NioEventLoopGroup(1);
                try {
                    ChannelFuture zhangChannelFuture = zhangBootstrap.group(clientGroup)
                            .channel(NioSocketChannel.class)
                            .remoteAddress(new InetSocketAddress("127.0.0.1", 6666))
                            .handler(new ZhangChannelInitializer())
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .connect().sync();

                    long start = System.currentTimeMillis();

                    Channel zhangChannel = zhangChannelFuture.channel();
                    for (int i = 0; i < MEET_COUNT; i++) {
                        zhangChannel.writeAndFlush(Z0);
                        if (i % 100 == 0) {
                            zhangChannel.flush();
                        }
                    }
                    zhangChannel.flush();

                    zhangChannel.closeFuture().sync();
                    liChannel.close();

                    System.out.println("会面完成,耗时：" + (System.currentTimeMillis() - start) + "ms");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    clientGroup.shutdownGracefully().syncUninterruptibly();
                }

            }).start();

            liChannel.closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully().syncUninterruptibly();
            childGroup.shutdownGracefully().syncUninterruptibly();
        }
    }

}
