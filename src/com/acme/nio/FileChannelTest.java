package com.acme.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    public static void main(String[] args) throws Exception{
        String str = "hello world";
        FileOutputStream fileOutputStream = new FileOutputStream("d://1.txt");
        final FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

    @Test
    public void read() throws  Exception{
        final File file = new File("d://1.txt");
        final FileInputStream fileInputStream = new FileInputStream(file);
        final FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        channel.read(byteBuffer);
//        final byte[] bytes = new byte[1024];
//        int i = 0;
//        while(byteBuffer.hasRemaining()){
//            bytes[i] = byteBuffer.get();
//            i++;
//        }
//        System.out.println(new String(bytes,0,bytes.length-1));

        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
