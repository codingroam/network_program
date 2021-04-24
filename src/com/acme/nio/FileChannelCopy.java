package com.acme.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopy {

    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        final FileOutputStream fileOutputStream = new FileOutputStream("2.txt",true);
        final FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        final FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        while(true){
            byteBuffer.clear();
            final int read = fileInputStreamChannel.read(byteBuffer);
            if(read==-1){
                break;
            }

            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
