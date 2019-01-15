package javaxx.servlet;

import com.hcy.httpserver.core.RequestObject;
import com.hcy.httpserver.core.ResponseObject;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 13:01
 * @Description: 指定的 servlet 规范 提供给开发者
 * @version 1.0
 * @since 1.0
 */
public interface Servlet {
    //处理核心业务
    void serivice(RequestObject requestObject, ResponseObject responseObject);
}
