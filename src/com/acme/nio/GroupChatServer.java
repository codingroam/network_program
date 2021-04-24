package com.acme.nio;

import jdk.nashorn.internal.ir.CallNode;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private final int PORT = 6668;

    public GroupChatServer(){
        try{
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listen(){
        try{
            while(true){
                final int select = selector.select(2000);
                if(select>0){
                    final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    final Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        final SelectionKey next = iterator.next();
                        //注册
                        if(next.isAcceptable()){
                            final SocketChannel accept = serverSocketChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector,SelectionKey.OP_READ);
                            System.out.println(accept.getRemoteAddress()+"上线");
                        }
                        //读且转发
                        if(next.isReadable()){
                            SocketChannel channel = null;
                            try{
                                final SelectableChannel channel1 = next.channel();
                                //System.out.println(channel instanceof channel1 );

                                channel = (SocketChannel)next.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                final int read = channel.read(byteBuffer);
                                if(read>0){
                                    final String msg = new String(byteBuffer.array());

                                    System.out.println("from "+channel.getRemoteAddress()+"的消息："+msg);
                                    //转发消息给其他客户端
                                    sendInfoToOtherClients(msg,channel);
                                }

                            }catch (Exception e){
                                System.out.println(channel.getRemoteAddress()+"离线了");
                                //取消注册
                                next.cancel();
                                //关闭通道
                                channel.close();

                            }

                        }

                    }
                    iterator.remove();
                }else{
                    //System.out.println(".....等待连接");
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendInfoToOtherClients(String msg,SocketChannel self){
        System.out.println("服务器转发消息中.....");
        selector.keys().forEach(u->{
            final Channel channel = u.channel();
            //排除自己
            if(channel instanceof SocketChannel&&channel!=self){
                final ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                SocketChannel c = null;
                try{
                    c = (SocketChannel)channel;
                    c.write(wrap);
                }catch (Exception e){
                    e.printStackTrace();
                    try{
                        System.out.println(c.getRemoteAddress()+"离线");
                        u.cancel();
                        c.close();
                    }catch(Exception e1){
                        e1.printStackTrace();
                    }

                }

            }
        });
    }

    public static void main(String[] args) {
        final GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
