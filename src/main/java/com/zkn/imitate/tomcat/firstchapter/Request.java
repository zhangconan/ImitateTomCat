package com.zkn.imitate.tomcat.firstchapter;

import com.zkn.imitate.tomcat.utils.StringUtil;

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
            StringBuffer sb = new StringBuffer(1024);
            while ((str = br.readLine()) != null) {
                if("".equals(str))
                    break;
                sb.append(str).append("\n");
            }
            String strHeader = sb.toString();
            System.out.println(strHeader);
            uri = StringUtil.parserUri(sb.toString()," ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUri(){

        return uri;
    }
}
