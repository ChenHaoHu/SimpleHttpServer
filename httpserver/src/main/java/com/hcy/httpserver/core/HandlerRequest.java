package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;
import javaxx.servlet.Servlet;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 00:18
 * @Description: 多线程处理客户端请求
 * @version 1.0
 * @since 1.0
 */
public class HandlerRequest {

    private ByteBuffer input = null;

    private ByteBuffer output = null;

    private BufferedReader br = null;

    private PrintWriter out = null;

    private Map<String, String> requestData = null;


    public HandlerRequest(ByteBuffer input, ByteBuffer output) {
        this.input = input;
        this.output = output;
    }

    public ByteBuffer run() {

        Logger.log("httpserver thread:"+ Thread.currentThread().getName());
        StringBuffer out = new StringBuffer();

        try {

            byte[] b = input.array();
            String input = new String(b);

            //解析请求的信息
            requestData = RequestParser.getRequestData(input);
            Logger.log("request :"+requestData);


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

                    out =  responseStaticPage(uriWithNoParameters,out);

                }else{

                    //访问动态数据 ————> 访问【servlet】
                    //show404Page(out);
                    out = responseServlet(uri,out);
                }
            }else{
                out = show404Page(out);
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return output.put(out.toString().getBytes());
    }

    /**
     * 访问servlet
     * @param uri
     * @param html
     */
    private StringBuffer responseServlet(String uri, StringBuffer html) throws Exception {

        //获取 RequestObject
        RequestObject requestObject = new RequestObject(uri);

        //获取 RequesObjecte
        ResponseObject responseObject = new ResponseObject(html);

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

                //拼接响应信息
                html.append("HTTP/1.1 200 OK\n");
                html.append("Content-Type:text/html;charset=utf-8\n\n");


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

                    return html;



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
                return show404Page(html);
            }


        }else{
            //没有这个项目名
            return show404Page(html);
        }
        return show404Page(html);
    }

    private StringBuffer show404Page(StringBuffer html) {
        // 404 页面


        //拼接响应信息
        html.append("HTTP/1.1 200 OK\n");
        html.append("Content-Type:text/html;charset=utf-8\n\n");
        html.append("<div style=\"text-align: center;font-size: 40px;color: red;\"> 404页面</div>");
        return html;

    }

    /**
     * 处理静态页面
     * @param uri
     */
    private StringBuffer responseStaticPage(String uri,StringBuffer html) throws IOException {

        BufferedReader fileReader = null;
        File file = null;


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



            //拼接响应信息
            html.append("HTTP/1.1 200 OK\n");
            html.append("Content-Type:text/html;charset=utf-8\n\n");

            while ((temp = fileReader.readLine())!=null){
                html.append(temp);
            }

            return html;

        }else{
            //404 页面
            return show404Page(html);
        }



    }
}
