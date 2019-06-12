package javaxx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 13:18
 * @Description: 规范 响应接口封装
 * @version 1.0
 * @since 1.0
 */
public interface ServletResponse {

    public abstract StringBuffer getWriter()
            throws IOException;

}
