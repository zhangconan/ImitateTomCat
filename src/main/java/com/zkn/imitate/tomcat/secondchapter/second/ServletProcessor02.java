package com.zkn.imitate.tomcat.secondchapter.second;

import com.zkn.imitate.tomcat.secondchapter.Request;
import com.zkn.imitate.tomcat.secondchapter.Response;
import com.zkn.imitate.tomcat.utils.Constants;
import com.zkn.imitate.tomcat.utils.FileSystemClassLoader;
import com.zkn.imitate.tomcat.utils.StringUtil;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by wb-zhangkenan on 2017/1/3.
 */
public class ServletProcessor02 {

    public void process(Request request, Response response) {

        String str = request.getUri();
        if(!StringUtil.isEmpty(str) && str.lastIndexOf("/") >= 0){
            str = str.substring(str.lastIndexOf("/")+1);
        }else {
            return;
        }
        FileSystemClassLoader fileClassLoader =
                new FileSystemClassLoader(Constants.WEB_ROOT);
        try {
            Class clazz = fileClassLoader.findClass(str);
            Servlet servlet = (Servlet) clazz.newInstance();
            //Request和Response的包装类
            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);
            servlet.service(requestFacade,responseFacade);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
