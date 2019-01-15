package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-14 23:03
 * @Description: 程序启动
 * @version 1.0
 * @since 1.0
 */
public class BootStrap {

    /**
     * 启动类主方法
     * @param args
     */
    public static void main(String[] args) {
        //程序入口
        start();
    }

    /**
     * 主程序入口
     */
    private static void start() {

        Logger.log("httpserver start");

        //服务器端socket
        ServerSocket serverSocket = null;

        //客户端socket serverSocket.accept();
        Socket clientSocket= null;

        try {
            //记录启动开始时间
            long start = System.currentTimeMillis();
            //从server.xml解析出port号
            int port = ServerParser.getport();
            //初始化服务器 创建服务器套接字 设置端口号
            serverSocket = new ServerSocket(port);
            //加载 web.xml 文件
            WebParser.init();
            //记录启动结束时间
            long end = System.currentTimeMillis();

            Logger.log("httpserver started: "+ (end - start)+" ms");

            //多线程处理客户端请求
            //死循环进行接收

            while(true){

                //获取来自客户端的socket
                clientSocket = serverSocket.accept();
                //多线程开启 解决请求 把来自客户端的socket传入
                new Thread(new HandlerRequest(clientSocket)).start();

            }


//            //接收请求
//            Socket accept = serverSocket.accept();
//            //获取输入流
//            BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
//            //打印输入流内容
//            String temp = null;
//
//            while((temp = br.readLine())!=null){
//                System.out.println(temp);
//            }



        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            //关闭socket
            try {
                if(serverSocket != null){
                    serverSocket.close();
                }
                if(clientSocket != null){
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
