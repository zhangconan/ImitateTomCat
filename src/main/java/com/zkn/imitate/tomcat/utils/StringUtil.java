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

    /**
     * 获取uri
     * @param str
     * @return
     */
    public static String parserUri(String str,String spliter) {
        if(StringUtil.isEmpty(str))
            return "";
        int indexFirst = str.indexOf(spliter);
        if(indexFirst != -1){//说明查找到了
            int indexSecond = str.indexOf(spliter,indexFirst+1);
            if(indexSecond > indexFirst)
                return str.substring(indexFirst+1,indexSecond);
        }
        return "";
    }
}
