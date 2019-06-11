package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;
import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: 简单DI年华
 * @Date: 19-6-11 20:53
 * @Description: 程序启动
 * @version 2.0
 * @since 1.0
 */
public class BootStrap  implements Runnable{



    static ServerSocketChannel ssc = null;
    static Selector selector = null;

    /**
     * 启动类主方法
     * @param args
     */
    public static void main(String[] args) {
        //程序入口
        BootStrap b = new BootStrap();
        b.start();
        new Thread(b).start();
    }

    /**
     * 主程序入口
     */
    private  void start() {

        Logger.log("httpserver start");


        try{
            long start = System.currentTimeMillis();
            //打开一个通道
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(ServerParser.getport()));
            ssc.configureBlocking(false);
            selector = Selector.open();
            SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor());
            //加载 web.xml 文件
            WebParser.init();
            //记录启动结束时间
            long end = System.currentTimeMillis();

            Logger.log("httpserver started: "+ (end - start)+" ms");


            //启动完毕 开始监听






        }catch (Exception e){
            Logger.log("启动出现错误");
        }

    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable)(key.attachment());
        if (r!=null){
            r.run();
        }
    }

    public void run() {
        try {
            while (true){
                int events = selector.select();
                if (events>0){
                    Logger.log("selector get something");
                    Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                    while (selectionKeys.hasNext()){
                        SelectionKey key = selectionKeys.next();
                        Logger.log("dispath something");
                        dispatch(key);

                    }
                    selector.selectedKeys().clear();
                }
            }
        }catch (Exception e){
            System.out.println(e);
            Logger.log("出问题了");
        }
    }

    class Acceptor implements Runnable{

        public void run() {
            try{
                Logger.log("accept something");
                SocketChannel c = ssc.accept();
                if (c != null){
                    new Handler(selector,c);
                }
            }catch (Exception e){

            }
        }
    }



}
