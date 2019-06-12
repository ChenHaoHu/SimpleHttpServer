package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @ClassName: Handler
 * @Author: hcy
 * @Description:
 * @Date: 2019-06-11 22:24
 * @Version: 1.0
 **/
public class Handler implements Runnable {

    SocketChannel socket = null;
    SelectionKey sk = null;
    int MAXIN = 1024 * 10 ;
    int MAXOUT = 1024 * 10 ;
    ByteBuffer input = ByteBuffer.allocate(MAXIN);
    ByteBuffer output = ByteBuffer.allocate(MAXOUT);
    static final int READING = 0, SENDING = 1; int state = READING;

    public void run() {
        try{
            if (state == READING) read();
            else if (state == SENDING) send();
        }catch (Exception e){
            System.out.println(e);
        }

    }

    void read() throws IOException{
        Logger.log("read something");
        socket.read(input);
        process();
        state = SENDING;
        sk.interestOps(SelectionKey.OP_WRITE);
    }

    void send() throws Exception{
        Logger.log("send something");
        output.flip();
        socket.write(output);
        sk.cancel();
        socket.close();;
    }


    Handler(Selector sel, SocketChannel c) throws IOException {
        Logger.log("handler");
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();

    }

    void process(){

        StringBuffer html = new StringBuffer();
        //拼接响应信息
        output = new HandlerRequest(input,output).run();
    }
}
