package com.acme.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6668;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    public GroupChatClient(){
        try{
            selector = Selector.open();
            socketChannel = socketChannel.open(new InetSocketAddress(HOST,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            userName = socketChannel.getLocalAddress().toString();
            System.out.println(userName+" is ok...");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendInfo(String info){
        info = userName+" 说 "+info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void readInfo(){
        try{
            int readChannels = selector.select();
            if(readChannels>0){
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    //todo
                    final SelectionKey next = iterator.next();
                    if(next.isReadable()){
                        final SocketChannel channel = (SocketChannel)next.channel();
                        final ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);
                        final String msg = new String(allocate.array());
                        System.out.println(msg);
                    }

                }
                iterator.remove();
            }else{
                //System.out.println("没有可用的通道...");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(()->{
            while(true){
                groupChatClient.readInfo();
                try{
                    Thread.currentThread().sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        final Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            final String next = scanner.nextLine();
            groupChatClient.sendInfo(next);
        }


    }
}
