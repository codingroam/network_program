package com.acme.netty.http;

import com.acme.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {

    public static void main(String[] args) {
        //处理连接请求
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //客户端业务处理交给workerGroup
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            //创建服务区端启动的对象，配置参数
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置两个线程组
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new TestServerInitializer());//给我们的workerGroup管道的EventLoop对应的管道设置处理器


            //绑定一个端口并且同步，生成了一个channelFuture对象
            final ChannelFuture cf = serverBootstrap.bind(6688).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
