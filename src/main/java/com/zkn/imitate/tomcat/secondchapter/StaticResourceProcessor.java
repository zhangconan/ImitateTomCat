package com.zkn.imitate.tomcat.secondchapter;


/**
 * Created by wb-zhangkenan on 2016/12/29.
 */
public class StaticResourceProcessor {

    public void process(Request request,Response response){

        response.sendStaticResource(request.getUri());
    }
}
