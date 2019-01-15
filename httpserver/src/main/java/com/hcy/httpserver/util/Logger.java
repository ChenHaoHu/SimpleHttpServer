package com.hcy.httpserver.util;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-14 23:04
 * @Description: 日志工具类
 * @version 1.0
 * @since 1.0
 */
public class Logger {

    public Logger() {
    }

    /**
     * 打印日志
     * 日志格式
     * @param msg
     */
    public static void log(String msg){
        String out = " [INFO] "+ DateUtil.getCurrentTime()+" "+msg;
        System.out.println(out);
    }
}
