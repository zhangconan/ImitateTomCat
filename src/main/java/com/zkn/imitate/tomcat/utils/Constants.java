package com.zkn.imitate.tomcat.utils;

import java.io.File;

/**
 * Created by wb-zhangkenan on 2016/12/29.
 */
public class Constants {
    /**
     * webapp目录
     */
    public static String WEB_PATH = System.getProperty("user.dir")+ File.separator+"webapp";
    /**
     * 关闭的请求
     */
    public static String SHUT_DOWN = "/shutdown";
    /**
     *  根目录
     */
    public static String WEB_ROOT = System.getProperty("user.dir")+ File.separator+"webroot";
}
