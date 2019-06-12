package com.hcy.httpserver.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 00:30
 * @Description: 请求头解析
 * @version 1.0
 * @since 1.0
 */
public class RequestParser {


    /**
     *  解析请求信息
     * @param input
     * @return
     */
    public static Map<String,String> getRequestData(String input) {

        /**
         *      GET /xx/yy/zz HTTP/1.1
         *      Host: localhost:8085
         *      Connection: keep-alive
         */
        Map<String,String> requestData = new HashMap<String, String>();


        String temp = input;
        char c = temp.charAt(0);
        if (c == '\u0000'){
            return requestData;
        }
        temp = temp.substring(0, 30);
        System.out.println(temp);
        //进行解析
        if(temp != null){
            // GET /xx/yy/zz HTTP/1.1
            //根据空格分割成三部分 分别是 【请求方法】、【请求uri】、【请求协议及版本号】

            String[] s = temp.split(" ");
            requestData.put("method",s[0]);
            requestData.put("uri",s[1]);
            requestData.put("protocol",s[2]);

            /**
             * 其他参数在应用中不涉及 不进行采集
             * 采集下一行  temp = br.readLine(); 即可
             */
        }
        return requestData;
    }
}
