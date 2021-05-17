package com.acme.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.nashorn.internal.ir.CallNode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组来管理所有的channel

    //GlobalEventExecutor是一个全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //连接被建立后执行的函数
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        //将客户端加入聊天（服务器）的消息告诉所有在channelGroup里的客户端
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天~"+sdf.format(new Date())+"\n");
        channelGroup.add(channel);
    }

    //断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了~"+sdf.format(new Date())+"\n");
        System.out.println("在线人数"+channelGroup.size());
    }

    //表示channel处于一个活动的状态，表示XX上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        //获取到当前channel
        final Channel channel = ctx.channel();

        //遍历channelGruop根据不同的情况发送不同的消息
        channelGroup.forEach(ch->{
            if(channel!=ch){//不是当前channel，转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送消息:"+msg+"\n");

            }else{
                ch.writeAndFlush("[自己]发送了消息"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.close();
    }
}
