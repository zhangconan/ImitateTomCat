package com.zkn.imitate.tomcat.thirdchapter.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 * 这是一个连接器类，它的主要作用是创建一个服务器套接字用来处理HTTP请求。
 */
public class HttpConnector implements Runnable{
    //终止线程的标识
    private boolean stopped;
    //请求的类型
    private String scheme = "http";

    /**
     * 这个方法主要有三个作用：
     *  1、等待HTTP请求
     *  2、为每个请求创建HttpProcessor实例
     *  3、调用HttpProcessor的process方法处理HTTP请求。
     * 这里以后可以优化为放到线程池里进行处理
     */
    public void run() {

        ServerSocket serverSocket = null;
        int port = 8080;//端口号
        try{
            //创建serverSocket的实例
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        while(!stopped){
            Socket socket = null;
            try {
                //创建客户端实例
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            //请求处理
            HttpProcessor httpProcessor = new HttpProcessor(this);
            httpProcessor.process(socket);
        }
    }

    public void start(){
        //启动客户端实例
        Thread httpConnector = new Thread(this);
        httpConnector.start();
    }

    public String getScheme() {
        return scheme;
    }
}
