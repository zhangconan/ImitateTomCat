package com.zkn.imitate.tomcat.utils;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public final class RequestUtil {

    /**
     * 解析Cookie Cookies的格式为：Cookie: userName=budi; password=pwd;
     * @param header
     * @return
     */
    public static Cookie[] parseCookieHeader(String header) {
        if(StringUtil.isEmpty(header))
            return new Cookie[0];
        List<Cookie> cookieList = new ArrayList<>();
        while (header.length() > 0){
            int semicolon = header.indexOf(';');
            if(semicolon <= 0)
                break;
            String token = header.substring(0,semicolon);
            int equals = token.indexOf('=');
            if(equals > 0){
                String name = token.substring(0,equals).trim();
                String value = token.substring(equals+1).trim();
                cookieList.add(new Cookie(name,value));
            }
            if(semicolon < header.length())
                header = header.substring(semicolon+1);
            else
                header = "";
        }








        return null;
    }
}
