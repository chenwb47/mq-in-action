package com.doodl6.mq.helper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息解析协议，先获取4个字节转成int，再根据int的值读取相应长度的byte数组转换成内容
 */
public class ChatDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.isReadable(4)) {
            int saveReaderIndex = in.readerIndex();
            int msgLength = in.readInt();
            if (in.isReadable(msgLength)) {
                byte[] msgBytes = new byte[msgLength];
                in.readBytes(msgBytes);
                String chatMsg = new String(msgBytes, StandardCharsets.UTF_8);
                out.add(chatMsg);
            } else {
                //消息长度不够，跳出循环等待下次触发解析
                in.readerIndex(saveReaderIndex);
                break;
            }
        }
        System.out.println("ChatDecoder");
    }
}
