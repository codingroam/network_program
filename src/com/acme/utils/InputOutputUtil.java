package com.acme.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public  class InputOutputUtil {
    public static void handlerSocket(Socket accept){
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
