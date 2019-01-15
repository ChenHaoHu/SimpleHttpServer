package com.hcy.httpserver.core;

import javaxx.servlet.Servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 14:15
 * @Description: servlet 实例池
 * @version 1.0
 * @since 1.0
 */
public class ServletPool {
   private  static Map<String, List<Servlet>> servletMap = new HashMap<String, List<Servlet>>();

   public static  void put(String urlPattern, Servlet servlet){
       List<Servlet> servlets = null;
       servlets = servletMap.get(urlPattern);

       if (servlets == null || servlets.size() == 0){
             servlets = new ArrayList<Servlet>();
       }
       servlets.add(servlet);
       servletMap.put(urlPattern,servlets);

   }

    public static Servlet get(String urlPattern){

        List<Servlet> servlets = servletMap.get(urlPattern);

        if (servlets == null || servlets.size() == 0){
            return null;
        }else {
            Servlet servlet = servlets.get(0);
            servlets.remove(0);
            return servlet;
        }

    }
}
