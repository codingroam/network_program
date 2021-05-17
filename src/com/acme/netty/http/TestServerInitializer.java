package com.acme.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        //HttpServerCodec是netty提供的处理http的编解码器
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}
