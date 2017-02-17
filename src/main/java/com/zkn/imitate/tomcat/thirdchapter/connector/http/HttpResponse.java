package com.zkn.imitate.tomcat.thirdchapter.connector.http;

import com.zkn.imitate.tomcat.thirdchapter.connector.ResponseStream;
import com.zkn.imitate.tomcat.thirdchapter.connector.ResponseWriter;
import com.zkn.imitate.tomcat.utils.Constants;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public class HttpResponse implements HttpServletResponse {

    /**
     * 输出流
     */
    private OutputStream outputStream;
    /**
     * 请求对象
     */
    private HttpRequest request;
    private static final int BUFFER_SIZE = 1024;
    private PrintWriter writer;
    protected byte[] buffer = new byte[BUFFER_SIZE];
    protected int bufferCount = 0;
    /**
     * The actual number of bytes written to this Response.
     */
    protected int contentCount = 0;

    protected String encoding = null;

    public HttpResponse(OutputStream output) {
        this.outputStream = output;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void addCookie(Cookie cookie) {

    }

    public boolean containsHeader(String name) {
        return false;
    }

    public String encodeURL(String url) {
        return null;
    }

    public String encodeRedirectURL(String url) {
        return null;
    }

    public String encodeUrl(String url) {
        return null;
    }

    public String encodeRedirectUrl(String url) {
        return null;
    }

    public void sendError(int sc, String msg) throws IOException {

    }

    public void sendError(int sc) throws IOException {

    }

    public void sendRedirect(String location) throws IOException {

    }

    public void setDateHeader(String name, long date) {

    }

    public void addDateHeader(String name, long date) {

    }

    public void setHeader(String name, String value) {

    }

    public void addHeader(String name, String value) {

    }

    public void setIntHeader(String name, int value) {

    }

    public void addIntHeader(String name, int value) {

    }

    public void setStatus(int sc) {

    }

    public void setStatus(int sc, String sm) {

    }

    public int getStatus() {
        return 0;
    }

    public String getHeader(String name) {
        return null;
    }

    public Collection<String> getHeaders(String name) {
        return null;
    }

    public Collection<String> getHeaderNames() {
        return null;
    }

    public String getCharacterEncoding() {
        if (encoding == null)
            return ("ISO-8859-1");
        else
            return encoding;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {

        ResponseStream newStream = new ResponseStream(this);
        newStream.setCommit(false);
        OutputStreamWriter osr =
                new OutputStreamWriter(newStream, getCharacterEncoding());
        writer = new ResponseWriter(osr);
        return writer;
    }

    public void setCharacterEncoding(String charset) {

    }

    public void setContentLength(int len) {

    }

    public void setContentLengthLong(long len) {

    }

    public void setContentType(String type) {

    }

    public void setBufferSize(int size) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale loc) {

    }

    public Locale getLocale() {
        return null;
    }

    public void write(int b) throws IOException {
        if (bufferCount >= buffer.length)
            flushBuffer();
        buffer[bufferCount++] = (byte) b;
        contentCount++;
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        // If the whole thing fits in the buffer, just put it there
        if (len == 0)
            return;
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            contentCount += len;
            return;
        }

        // Flush the buffer and start writing full-buffer-size chunks
        flushBuffer();
        int iterations = len / buffer.length;
        int leftoverStart = iterations * buffer.length;
        int leftoverLen = len - leftoverStart;
        for (int i = 0; i < iterations; i++)
            write(b, off + (i * buffer.length), buffer.length);

        // Write the remainder (guaranteed to fit in the buffer)
        if (leftoverLen > 0)
            write(b, off + leftoverStart, leftoverLen);
    }

    public void finishResponse() {
        // sendHeaders();
        // Flush and close the appropriate output mechanism
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    public void sendStaticResource() throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(Constants.WEB_PATH, request.getRequestURI());
            if (file.exists() && !file.isDirectory()) {
                if (file.canRead()) {
                    fis = new FileInputStream(file);
                    int flag = 0;
                    byte[] bytes = new byte[1024];
                    while ((flag = fis.read(bytes)) != -1) {
                        outputStream.write(bytes);
                    }
                }
            } else {
                PrintWriter printWriter = getWriter();
                //这里用PrintWriter字符输出流，设置自动刷新
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
