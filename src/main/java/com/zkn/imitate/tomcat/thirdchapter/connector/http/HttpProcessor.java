package com.zkn.imitate.tomcat.thirdchapter.connector.http;


import com.zkn.imitate.tomcat.exception.ServletException;
import com.zkn.imitate.tomcat.thirdchapter.ServletProcessor;
import com.zkn.imitate.tomcat.thirdchapter.StaticResourceProcessor;
import com.zkn.imitate.tomcat.utils.RequestUtil;
import com.zkn.imitate.tomcat.utils.StringManager;

import javax.servlet.http.Cookie;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 * 这个类相当于第二节中的HttpServer
 */
public class HttpProcessor {

    private HttpConnector httpConnector;
    private HttpRequest request;
    private HttpResponse response;
    /**
     * 请求行
     */
    private HttpRequestLine httpRequestLine = new HttpRequestLine();
    protected StringManager sm = StringManager.getManager("com.zkn.imitate.tomcat.thirdchapter.connector.http");

    public HttpProcessor(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) {

        SocketInputStream input = null;
        OutputStream output = null;
        try {
            input = new SocketInputStream(socket.getInputStream(),2048);
            //响应输出流
            output = socket.getOutputStream();
            //模拟Request对象
            request = new HttpRequest(input);
            //模拟Response对象
            response = new HttpResponse(output);
            response.setRequest(request);
            response.setHeader("Server","Pyrmont Servlet Container");
            parseRequest(input,output);//解析请求信息
            parseHeader(input);//解析头信息
            //处理Servlet
            if(request.getRequestURI().startsWith("/servlet/")){
                ServletProcessor processor = new ServletProcessor();
                processor.process(request,response);
            }
            //处理静态资源
            if(request.getRequestURI().startsWith("/static/")){
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request,response);
            }
            //关闭socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseHeader(SocketInputStream input) throws ServletException {

        while (true){
            HttpHeader header = new HttpHeader();
            //解析头部
            input.readHeader(header);
            if(header.nameEnd == 0){
                if(header.valueEnd == 0){
                    return;
                }else{
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
                }
            }
            String name = new  String(header.name,0,header.nameEnd);
            String value = new String(header.value,0,header.valueEnd);
            request.addHeader(name,value);
            if("cookie".equals(name)){
                Cookie cookies[] = RequestUtil.parseCookieHeader(value);
                for(int i=0;i<cookies.length;i++){
                    if (cookies[i].getName().equals("jsessionid")) {
                        if (!request.isRequestedSessionIdFromCookie()) {
                            // 只要第一个Cookie
                            request.setRequestedSessionId(cookies[i].getValue());
                            request.setRequestedSessionCookie(true);
                            request.setRequestedSessionURL(false);
                        }
                    }
                    request.addCookie(cookies[i]);
                }
            }
            if (name.equals("content-length")) {
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                }
                catch (Exception e) {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
                }
                request.setContentLength(n);
            }
            if (name.equals("content-type")) {
                request.setContentType(value);
            }
        }
    }

    private void parseRequest(SocketInputStream input, OutputStream output) throws ServletException {
        //解析请求行
        input.readRequestLine(httpRequestLine);
        //请求行的方法
        String method = new String(httpRequestLine.method,0,httpRequestLine.methodEnd);
        //请求行的uri
        String uri = null;
        //请求行的协议
        String protocol = new String(httpRequestLine.protocol,0,httpRequestLine.protocolEnd);
        if(method == null || method.length() < 1){
            throw new ServletException("Missing HTTP request method");
        }
        if(httpRequestLine.getUriEnd() < 1){
            throw new ServletException("Missing HTTP request URI");
        }
        //解析请求参数
        int question = httpRequestLine.indexOf("?");
        if(question >= 0){
            //设置请求参数
            request.setQueryString(new String(httpRequestLine.uri,question+1,httpRequestLine.uriEnd-question-1));
            uri = new String(httpRequestLine.uri,0,question);
        }else{
            request.setQueryString(null);
            uri = new String(httpRequestLine.uri,0,httpRequestLine.uriEnd);
        }
        if(!uri.startsWith("/")){
            //针对http://www.brainysoftware.com/index.html?name=Tarzan这种情况
            int pos = uri.indexOf("://");
            if(pos != -1){
                pos = uri.indexOf('/',pos+3);
                if(pos == -1){
                    uri = "";
                }else{
                    uri = uri.substring(pos);
                }
            }
        }
        //解析session
        String match = ";jsessionid=";
        int semicolon = uri.indexOf(match);
        if(semicolon >= 0){
            String rest = uri.substring(semicolon+match.length());
            int semicolon2 = rest.indexOf(';');
            if(semicolon2 >= 0){
                request.setRequestedSessionId(rest.substring(0,semicolon2));
                rest = rest.substring(semicolon2);
            }else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0,semicolon)+rest;
        }else{
            request.setRequestedSessionId(null);
            request.setRequestedSessionURL(false);
        }
        String normalizedUri = normalize(uri);
        request.setMethod(method);
        request.setProtocol(protocol);
        if(normalizedUri != null){
            request.setRequestURI(normalizedUri);
        }else{
            request.setRequestURI(uri);
        }
        if(normalizedUri == null){
            throw  new ServletException("Invalid URI:"+uri+"");
        }
    }
    //解析URI出现的各种情况
    private String normalize(String uri) {
        if (uri == null)
            return null;
        String normalized = uri;

        // 以/~开头的
        if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
            normalized = "/~" + normalized.substring(4);

        // 如果存在 '%', '/', '.' and '\' 特殊字符
        if ((normalized.indexOf("%25") >= 0) // %
                || (normalized.indexOf("%2F") >= 0) // /
                || (normalized.indexOf("%2E") >= 0) // .
                || (normalized.indexOf("%5C") >= 0) // \
                || (normalized.indexOf("%2f") >= 0)
                || (normalized.indexOf("%2e") >= 0)
                || (normalized.indexOf("%5c") >= 0)) {
            return null;
        }

        if (normalized.equals("/."))
            return "/";
        //转换 //
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;
        //转换 //
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // 转换 "/./"
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // 转换 "/../"
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return null;
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // 如果是 "/..."
        // 会进入根目录
        if (normalized.indexOf("/...") >= 0)
            return (null);

        return normalized;
    }
}
