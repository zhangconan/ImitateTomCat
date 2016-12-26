package com.zkn.imitate.tomcat.firstchapter;

import java.io.OutputStream;

/**
 * Created by zkn on 2016/12/26.
 * 请求的响应类（Tomcat封装响应的类是org.apache.coyote.Response）
 * @see org.apache.coyote.Response
 */
public class Response {
    /**
     * 输出流
     */
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


}
