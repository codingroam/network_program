package com.acme.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketSingle {
    /**
     * 每次只能有一个客户端连接
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while(true){
            try(Socket accept = serverSocket.accept();){
                InputStream inputStream = accept.getInputStream();
                byte[] bytes = new byte[1024];
                int read = 0;
                while((read = inputStream.read(bytes))!=-1){
                    System.out.println(new String(bytes,0,read));
                }
            }

        }




    }
}
