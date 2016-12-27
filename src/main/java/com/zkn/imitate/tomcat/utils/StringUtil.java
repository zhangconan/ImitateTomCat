package com.zkn.imitate.tomcat.utils;

/**
 * Created by wb-zhangkenan on 2016/12/27.
 */
public class StringUtil {
    /**
     * 字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str == null || "".equals(str))
            return true;
        return false;
    }
}
