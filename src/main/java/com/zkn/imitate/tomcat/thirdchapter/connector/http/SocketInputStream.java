package com.zkn.imitate.tomcat.thirdchapter.connector.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public class SocketInputStream extends InputStream {

    private InputStream inputStream;

    //每次读取的字节数
    private int length;

    public int read() throws IOException {
        return 0;
    }

    public SocketInputStream() {
    }

    public SocketInputStream(InputStream inputStream, int length) {
        this.inputStream = inputStream;
        this.length = length;
    }

    public void readRequestLine(HttpRequestLine httpRequestLine) {
    }

    public void readHeader(HttpHeader header) {
    }
}
