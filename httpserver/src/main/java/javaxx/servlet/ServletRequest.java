package javaxx.servlet;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 13:16
 * @Description: 规范：请求接口封装
 * @version 1.0
 * @since 1.0
 */
public interface ServletRequest {

    String getParameterValue(String key);
    String[] getParameterValues(String key);



}
