package com.zkn.imitate.tomcat.thirdchapter.startup;

import com.zkn.imitate.tomcat.thirdchapter.connector.http.HttpConnector;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public class Bootstrap {

    public static void main(String[] args){

        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}
