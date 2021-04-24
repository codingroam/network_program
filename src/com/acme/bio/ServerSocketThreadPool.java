package com.acme.bio;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketThreadPool {

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(1111);
        final ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            Socket accept = serverSocket.accept();
            executorService.execute(()->{
                handler(accept);

            });

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
