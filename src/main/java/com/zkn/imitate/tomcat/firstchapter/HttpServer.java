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

    public static String WEB_PATH = System.getProperty("user.dir")+ File.separator+"webapp";

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
                //解析请求
                request.parserRequest();
                Response response = new Response(socket.getOutputStream());
                //向页面写入相应输出
                response.writeResponse(request.getUri());
                shutdown = SHUT_DOWN.equals(request.getUri());
                //关闭socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
