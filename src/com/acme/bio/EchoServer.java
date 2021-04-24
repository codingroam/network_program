package com.acme.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        try(Socket accept = serverSocket.accept()){
            final InputStream inputStream = accept.getInputStream();
            final OutputStream outputStream = accept.getOutputStream();
            try(Scanner scanner = new Scanner(inputStream)){
                final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream),true);
                printWriter.println("hello,I am server!");
                final Scanner scanner1 = new Scanner(System.in);
                while(scanner.hasNextLine()){
                    System.out.println(scanner.nextLine());
                    printWriter.println(scanner1.nextLine());
                }
            }
        }
    }
}
