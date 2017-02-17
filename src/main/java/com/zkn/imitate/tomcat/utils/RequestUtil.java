package com.zkn.imitate.tomcat.utils;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wb-zhangkenan on 2017/2/16.
 */
public final class RequestUtil {

    /**
     * 解析Cookie Cookies的格式为：Cookie: userName=budi; password=pwd;
     * @param header
     * @return
     */
    public static Cookie[] parseCookieHeader(String header) {
        if(StringUtil.isEmpty(header))
            return new Cookie[0];
        List<Cookie> cookieList = new ArrayList<>();
        while (header.length() > 0){
            int semicolon = header.indexOf(';');
            if(semicolon <= 0)
                break;
            String token = header.substring(0,semicolon);
            int equals = token.indexOf('=');
            if(equals > 0){
                String name = token.substring(0,equals).trim();
                String value = token.substring(equals+1).trim();
                cookieList.add(new Cookie(name,value));
            }
            if(semicolon < header.length())
                header = header.substring(semicolon+1);
            else
                header = "";
        }
        return cookieList.toArray(new Cookie[0]);
    }

    public static void parseParameters(Map map, String data, String encoding)
            throws UnsupportedEncodingException {

        if (!StringUtil.isEmpty(data)) {
            int len = data.length();
            byte[] bytes = data.getBytes(encoding);
            parseParameters(map, bytes, encoding);
        }
    }

    public static void parseParameters(Map map, byte[] data, String encoding)
            throws UnsupportedEncodingException {

        if (data != null && data.length > 0) {
            int    pos = 0;
            int    ix = 0;
            int    ox = 0;
            String key = null;
            String value = null;
            while (ix < data.length) {
                byte c = data[ix++];
                switch ((char) c) {
                    case '&':
                        value = new String(data, 0, ox, encoding);
                        if (key != null) {
                            putMapEntry(map, key, value);
                            key = null;
                        }
                        ox = 0;
                        break;
                    case '=':
                        key = new String(data, 0, ox, encoding);
                        ox = 0;
                        break;
                    case '+':
                        data[ox++] = (byte)' ';
                        break;
                    case '%':
                        data[ox++] = (byte)((convertHexDigit(data[ix++]) << 4)
                                + convertHexDigit(data[ix++]));
                        break;
                    default:
                        data[ox++] = c;
                }
            }
            //The last value does not end in '&'.  So save it now.
            if (key != null) {
                value = new String(data, 0, ox, encoding);
                putMapEntry(map, key, value);
            }
        }
    }

    private static void putMapEntry( Map map, String name, String value) {
        String[] newValues = null;
        String[] oldValues = (String[]) map.get(name);
        if (oldValues == null) {
            newValues = new String[1];
            newValues[0] = value;
        } else {
            newValues = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[oldValues.length] = value;
        }
        map.put(name, newValues);
    }

    /**
     * Convert a byte character value to hexidecimal digit value.
     *
     * @param b the character value byte
     */
    private static byte convertHexDigit(byte b) {
        if ((b >= '0') && (b <= '9')) return (byte)(b - '0');
        if ((b >= 'a') && (b <= 'f')) return (byte)(b - 'a' + 10);
        if ((b >= 'A') && (b <= 'F')) return (byte)(b - 'A' + 10);
        return 0;
    }
}
