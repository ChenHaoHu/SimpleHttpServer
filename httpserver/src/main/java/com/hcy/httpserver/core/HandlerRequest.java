package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;
import javaxx.servlet.Servlet;

import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 00:18
 * @Description: 多线程处理客户端请求
 * @version 1.0
 * @since 1.0
 */
public class HandlerRequest implements Runnable{

    private Socket clientSocket = null;

    private BufferedReader br = null;

    private PrintWriter out = null;

    private Map<String, String> requestData = null;


    public HandlerRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {

        Logger.log("httpserver thread:"+ Thread.currentThread().getName());

        try {

            //获取输入流
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //解析请求的信息
            requestData = RequestParser.getRequestData(br);
            //获取输入流
            out =  new PrintWriter(clientSocket.getOutputStream());

            //todo:解决多次请求的问题 初步使用 requestData.get("uri") != null 强制转 404 页面
            if(requestData.get("uri") != null){

                //获取请求的uri 例如：【/xx/yy/zz】
                String uri = requestData.get("uri");

                //获取没有参数的uri
                RequestObject requestObject = new RequestObject(uri);
                String  uriWithNoParameters = requestObject.getUriWithNoParameters();

                //打印日志
                Logger.log("httpserver uri: "+ uriWithNoParameters);

                //后缀为 html 或者 htm 的请求为访问静态页面
                if (uriWithNoParameters.toLowerCase().endsWith("html") || uriWithNoParameters.toLowerCase().endsWith("htm") ){

                    responseStaticPage(uriWithNoParameters,out);

                }else{

                    //访问动态数据 ————> 访问【servlet】
                    //show404Page(out);
                    responseServlet(uri,out);
                }
            }else{
                show404Page(out);
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                out.close();
            }

            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 访问servlet
     * @param uri
     * @param out
     */
    private void responseServlet(String uri, PrintWriter out) throws Exception {

        //获取 RequestObject
        RequestObject requestObject = new RequestObject(uri);
        //获取 RequesObjecte
        ResponseObject responseObject = new ResponseObject(out);

        //访问servlet

        //检查是否在 web.xml 中配置过

        //获取appname
        String appName = requestObject.getAppName();

        //判断是否有这个项目被配置
        if(WebParser.webData.containsKey(appName)){


            //有这个项目
            Map<String, String> mappingMap = WebParser.webData.get(appName);
            //判断是否有这个请求路径
            String urlPattern = requestObject.getUrlPattern();
            Logger.log("urlPattern: "+urlPattern);
            if (mappingMap.containsKey(urlPattern)){
                //处理一下 响应头
                StringBuffer html = new StringBuffer();
                //拼接响应信息
                html.append("HTTP/1.1 200 OK\n");
                html.append("Content-Type:text/html;charset=utf-8\n\n");
                out.print(html);

                //含有这个 urlPattern
                //根据 urlPattern 获取 classPath
                String classPath = mappingMap.get(urlPattern);
                Logger.log("classPath: " + classPath);
                //反射 ——> 加载【servlet】
                try {

                    //检查 servlet 缓存池  是否有现成的

                    //取出
                    Servlet servletNew = ServletPool.get(urlPattern);

                    if (servletNew == null){

                        //获取 class
                        Class c = Class.forName(classPath);
                        Object o = c.newInstance();
                        //解耦 转化为【servlet】
                        Servlet servlet = (Servlet)o;
                        //执行 service 方法
                        servlet.serivice(requestObject,responseObject);
                        Logger.log("servlet: "+ servlet);
                        //放回
                        ServletPool.put(urlPattern,servlet);

                    }else{
                        //执行 service 方法
                        servletNew.serivice(requestObject,responseObject);
                        Logger.log("servlet: "+ servletNew);
                        //放回
                        ServletPool.put(urlPattern,servletNew);
                    }


                    if (out!=null){
                        out.flush();
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }else{
                //没有这个 urlPattern
                // 404
                Logger.log("urlPattern no way");
                show404Page(out);
            }


        }else{
            //没有这个项目名
            show404Page(out);
        }

    }

    private void show404Page(PrintWriter out) {
        // 404 页面
        StringBuffer html = new StringBuffer();

        //拼接响应信息
        html.append("HTTP/1.1 200 OK\n");
        html.append("Content-Type:text/html;charset=utf-8\n\n");

        html.append("<div style=\"text-align: center;font-size: 40px;color: red;\"> 404页面</div>");

        out.print(html);
        out.flush();
    }

    /**
     * 处理静态页面
     * @param uri
     */
    private void responseStaticPage(String uri,PrintWriter out)  {

       BufferedReader fileReader = null;
       File file = null;

      try{
          //这里访问静态页面 输出静态页面到输出流即可
          String[] dirs = uri.split("/");

          StringBuffer path = new StringBuffer();

          for (int i = 0; i < dirs.length; i++) {
              path.append(File.separator+dirs[i]);
          }

          //拼接文件路径
          //todo:解决路径问题 初步认定为是本项目是模块项目导致根路径为模块目录
          file = new File("httpserver"+File.separator+
                  "webapps"+path);

         // Logger.log(file.getPath());

          //判断文件在不在
          if(file.exists()){
              //获取文件流
               fileReader = new BufferedReader(new FileReader(file));

              String temp = null;

              StringBuffer html = new StringBuffer();

              //拼接响应信息
              html.append("HTTP/1.1 200 OK\n");
              html.append("Content-Type:text/html;charset=utf-8\n\n");

              while ((temp = fileReader.readLine())!=null){
                  html.append(temp);
              }

              out.print(html);
              out.flush();


          }else{
              //404 页面
              show404Page(out);
          }
      }catch (IOException e){
          e.fillInStackTrace();
      }finally {
          if(fileReader!=null){
              try {
                  fileReader.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
    }
}
