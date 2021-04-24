package com.acme.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSocketThread {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while(true){
            Socket accept = serverSocket.accept();
            new Thread(()->{
                handler(accept);
            }).start();

        }


    }

    private static void handler(Socket accept) {
        try {
            Scanner scanner = new Scanner(accept.getInputStream());
            final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(accept.getOutputStream(),"gbk"),true);
            printWriter.println("已连接");
            while(scanner.hasNextLine()){
                System.out.println(scanner.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
