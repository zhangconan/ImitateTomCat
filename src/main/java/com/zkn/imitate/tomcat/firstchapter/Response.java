package com.zkn.imitate.tomcat.firstchapter;

import com.zkn.imitate.tomcat.utils.Constants;

import java.io.*;

/**
 * Created by zkn on 2016/12/26.
 * 请求的响应类（Tomcat封装响应的类是org.apache.coyote.Response）
 *
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

    public void writeResponse(String filePath) {
        FileInputStream fis = null;
        try {
            File file = new File(Constants.WEB_PATH,filePath);
            //如果文件存在的话
            if (file.exists() && !file.isDirectory()) {
                if(file.canRead()){
                    fis = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int flag = 0;
                    while ((flag = fis.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, flag);
                    }
                }
            } else {
                //这里用PrintWriter字符输出流，设置自动刷新
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.write("HTTP/1.1 404 File Not Found \r\n");
                printWriter.write("Content-Type: text/html\r\n");
                printWriter.write("Content-Length: 23\r\n");
                printWriter.write("\r\n");
                printWriter.write("<h1>File Not Found</h1>");
                printWriter.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
