package com.acme.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception{
        //创建serverSockerChannel(BIO中的serverSocket)
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //拿到selector对象
        final Selector selector = Selector.open();

        serverSocketChannel.bind(new InetSocketAddress(6666));

        //非阻塞

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.select(1000)==0){
                System.out.println("1秒没有客户端连接");
                continue;
            }

            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            final Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                final SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()){
                    final SocketChannel accept = serverSocketChannel.accept();
                    System.out.println("客户端连接成功："+accept);
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(selectionKey.isReadable()){
                    //通过key反向得到channel
                    final SocketChannel channel = (SocketChannel)selectionKey.channel();
                    //通过
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from 客户端："+new String(byteBuffer.array()));

                }
                //操作完要删除
                iterator.remove();

            }


        }


    }
}
