package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;

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
                //打印日志
                Logger.log("httpserver uri: "+ uri);

                //后缀为 html 或者 htm 的请求为访问静态页面
                if (uri.toLowerCase().endsWith("html") || uri.toLowerCase().endsWith("htm") ){

                    responseStaticPage(uri,out);


                }else{
                    show404Page(out);
                }
            }else{
                show404Page(out);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
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
