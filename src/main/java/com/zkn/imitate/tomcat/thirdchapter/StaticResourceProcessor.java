package com.zkn.imitate.tomcat.thirdchapter;

import com.zkn.imitate.tomcat.thirdchapter.connector.http.HttpRequest;
import com.zkn.imitate.tomcat.thirdchapter.connector.http.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public class StaticResourceProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
