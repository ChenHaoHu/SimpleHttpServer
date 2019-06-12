package com.hcy.httpserver.core;

import javaxx.servlet.ServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 12:54
 * @Description: 回复封装
 * @version 1.0
 * @since 1.0
 */
public class ResponseObject implements ServletResponse {

    private StringBuffer  pw = null;

    public ResponseObject(StringBuffer pw) {
        this.pw = pw;
    }
    public StringBuffer getPw() {
        return pw;
    }

    public void setPw(StringBuffer pw) {
        this.pw = pw;
    }


    public StringBuffer getWriter() throws IOException {
        return pw;
    }
}
