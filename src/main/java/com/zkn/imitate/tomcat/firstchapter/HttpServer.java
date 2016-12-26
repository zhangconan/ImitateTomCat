package com.zkn.imitate.tomcat.firstchapter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zkn on 2016/12/26.
 */
public class HttpServer {

    private static String WEB_PATH = System.getProperty("user.dir")+ File.separator+"webapp";

    private static String SHUT_DOWN = "/shutdown";

    public static void main(String[] args){

        await();
    }

    private static void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8003,1, InetAddress.getByName("127.0.0.1"));
            boolean shutdown = false;
            while (!shutdown){
                Socket socket = serverSocket.accept();
                Request request = new Request(socket.getInputStream());
                request.parserRequest();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
