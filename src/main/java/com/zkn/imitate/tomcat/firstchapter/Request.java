package com.zkn.imitate.tomcat.firstchapter;

import com.zkn.imitate.tomcat.utils.StringUtil;
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
            StringBuffer sb = new StringBuffer(1024);
            while ((str = br.readLine()) != null) {
                if("".equals(str))
                    break;
                sb.append(str).append("\n");
            }
            br = null;
            String strHeader = sb.toString();
            System.out.println(strHeader);
            uri = parserUri(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parserUri(String str) {
        if(StringUtil.isEmpty(str))
            return "";
        int indexFirst = str.indexOf(" ");
        if(indexFirst != -1){//说明查找到了
            int indexSecond = str.indexOf(" ",indexFirst+1);
            if(indexSecond > indexFirst)
               return str.substring(indexFirst+1,indexSecond);
        }
        return "";
    }

    public String getUri(){

        return uri;
    }
}
