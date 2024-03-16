package com.zhanghc.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/*
Echo Server 将会将接受到的数据的拷贝发送给客户端
 */
@ChannelHandler.Sharable // 此类中的实例可以 channel 共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 每个信息进入都会调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("server received : " + in.toString(CharsetUtil.UTF_8));
        // 将所接收的消息返回给发送者。注意，这还没有冲刷数据
        ctx.write(in);
    }

    // 处理器最后的 channelRead 是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 冲刷所有待审消息到远程节点
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE); // 主动关闭客户端的channel
    }

    // 读操作时捕获到异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();                //5
        ctx.close();
    }
}
