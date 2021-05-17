package com.acme.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyChatClient {

    private final String host;
    private final int port;

    public NettyChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
        final EventLoopGroup eventExecutors = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });
            final ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //得到channel
            final Channel channel = channelFuture.channel();
            System.out.println("-------"+channel.remoteAddress()+"----------");

            final Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg+"\r\n");
            }

        }finally {
            eventExecutors.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws  Exception {

        new NettyChatClient("127.0.0.1", 6669).run();
    }
}

