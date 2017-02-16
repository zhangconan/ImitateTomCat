package com.zkn.imitate.tomcat.firstchapter;

import com.zkn.imitate.tomcat.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zkn on 2016/12/26.
 */
public class HttpServer {

    public static void main(String[] args){
        await();
        //System.out.println(System.getProperty("user.dir"));
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
                shutdown = Constants.SHUT_DOWN.equals(request.getUri());
                //关闭socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
