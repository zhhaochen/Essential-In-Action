package com.zhanghc.server;

import com.zhanghc.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        // 创建并分配一个 NioEventLoopGroup 实例来处理事件的处理，如接受新的连接和读/写数据。
        EventLoopGroup group =  new NioEventLoopGroup();
        try {
            ServerBootstrap b  = new ServerBootstrap();
            b.group(group)
                    // 指定使用nio的传输channel
                    .channel(NioServerSocketChannel.class)
                    // 对外服务的端口
                    .localAddress(new InetSocketAddress(port))
                    /*
                    当一个新的连接被接受，一个新的子 Channel 将被创建，
                     ChannelInitializer 会添加我们EchoServerHandler 的实例到 Channel 的 ChannelPipeline。
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 绑定服务器，sync等待服务器关闭
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            // 关闭 channel 和块，直到它被关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关机的 EventLoopGroup，释放所有资源。
            group.shutdownGracefully().sync();
        }
    }
}
