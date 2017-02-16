package com.zkn.imitate.tomcat.secondchapter.second;

import com.zkn.imitate.tomcat.secondchapter.Request;
import com.zkn.imitate.tomcat.secondchapter.Response;
import com.zkn.imitate.tomcat.secondchapter.StaticResourceProcessor;
import com.zkn.imitate.tomcat.secondchapter.first.ServletProcessor01;
import com.zkn.imitate.tomcat.utils.Constants;
import com.zkn.imitate.tomcat.utils.StringUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wb-zhangkenan on 2016/12/29.
 */
public class HttpServer03 {

    public static void main(String[] args){

        await();
    }

    private static void await() {

        ServerSocket serverSocket = null;
        try {
            boolean shutDown = false;
            serverSocket = new ServerSocket(8004,1, InetAddress.getByName("127.0.0.1"));
            while (!shutDown){
                Socket socket = serverSocket.accept();
                Request request = new Request(socket.getInputStream());
                request.parseRequest();
                Response response = new Response(socket.getOutputStream());
                String uri = request.getUri();
                if(uri !=null && uri.startsWith("/favicon.ico")){

                }else if(!StringUtil.isEmpty(uri) && uri.startsWith("/static/")){
                    StaticResourceProcessor resouce = new StaticResourceProcessor();
                    resouce.process(request,response);
                }else{
                    ServletProcessor02 servletProcessor = new ServletProcessor02();
                    servletProcessor.process(request,response);
                }
                socket.close();
                shutDown = Constants.SHUT_DOWN.equals(request.getUri());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
