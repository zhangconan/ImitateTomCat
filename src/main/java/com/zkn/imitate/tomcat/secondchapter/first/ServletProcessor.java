package com.zkn.imitate.tomcat.secondchapter.first;

import com.zkn.imitate.tomcat.secondchapter.Request;
import com.zkn.imitate.tomcat.secondchapter.Response;
import com.zkn.imitate.tomcat.utils.Constants;
import com.zkn.imitate.tomcat.utils.StringUtil;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * Created by wb-zhangkenan on 2016/12/29.
 */
public class ServletProcessor {
    /**
     * 处理请求信息
     * @param request
     * @param response
     */
    public void process(Request request, Response response){
        //URI信息
        String str = request.getUri();
        String servletName = null;
        if(!StringUtil.isEmpty(str) && str.lastIndexOf("/") >= 0){
            servletName = str.substring(str.lastIndexOf("/")+1);
        }
        URLClassLoader classLoader = null;
        URL[] url = new URL[1];

        URLStreamHandler streamHandler = null;
        File classPath = new File(Constants.WEB_ROOT);

        try {
            //创建仓库位置
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            url[0] = new URL(null,repository,streamHandler);
            //URL类加载器
            classLoader = new URLClassLoader(url);
            //加载类
            Class clazz = classLoader.loadClass(servletName);
            Servlet servlet = (Servlet) clazz.newInstance();
            servlet.service(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
