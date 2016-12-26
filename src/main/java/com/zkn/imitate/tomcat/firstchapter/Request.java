package com.zkn.imitate.tomcat.firstchapter;

import jdk.internal.util.xml.impl.Input;

import java.io.*;

/**
 * Created by wb-zhangkenan on 2016/12/26.
 * 用来封装Request信息的（Tomcat中的请求信息是封装在org.apache.coyote.Request中的）。
 *
 * @see org.apache.coyote.Request
 */
public class Request {
    /**
     * 设置输入流
     */
    private InputStream inputStream;
    /**
     * 统一资源识别符
     */
    private String uri;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void parserRequest() {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        try {
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                if("".equals(str))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
