package com.app.email;

import com.hcy.httpserver.core.RequestObject;
import com.hcy.httpserver.core.ResponseObject;
import javaxx.servlet.Servlet;

import java.io.IOException;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 13:27
 * @Description: 开发的产品 servlet 发送邮件的app 不属于服务器项目的本体 输入产品
 * @version 1.0
 * @since 1.0
 */

public class EmailServlet  implements Servlet {

    /**
     * 实现了规范接口
     * @param requestObject
     * @param responseObject
     */
    public void serivice(RequestObject requestObject, ResponseObject responseObject) {
        String xx = requestObject.getParameterValue("xx");
        try {
            responseObject.getWriter().print("get the xx:"+xx);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
