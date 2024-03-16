package com.zhanghc.client;

import com.zhanghc.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 注意，客户端用 Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    // 指定使用nio的传输channel
                    .channel(NioSocketChannel.class)
                    // 要连接服务的地址
                    .remoteAddress(new InetSocketAddress(host, port))
                    /*
                    当一个新的连接被接受，一个新的子 Channel 将被创建，
                     ChannelInitializer 会添加我们 EchoClientHandler 的实例到 Channel 的 ChannelPipeline。
                     */
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 连接到远程;等待连接完成
            ChannelFuture f = b.connect().sync();

            // 阻塞直到 Channel 关闭
            f.channel().closeFuture().sync();
            System.out.println("close");
        } finally {
            // 调用 shutdownGracefully() 来关闭线程池和释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
