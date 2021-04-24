package com.acme.bio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        final Socket localhost = new Socket("localhost", 7777);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localhost.getInputStream()));
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(localhost.getOutputStream()),true);
        String s = null;
        Scanner scanner = new Scanner(System.in);
        while((s = bufferedReader.readLine())!=null){
            System.out.println(s);

            printWriter.println(scanner.nextLine());
        }


    }
}
