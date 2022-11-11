package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 该线程任务负责与指定的客户端进行HTTP交互
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            InputStream in = socket.getInputStream();
            int d;
            /*
                在浏览器输入路径:
                http://localhost:8088/index.html
                查看浏览器发送过来的请求内容

                注意：
                如果这里打桩输出的是乱码，则说明浏览器地址栏输入测试路径时没有输入
                最开始的http://。这会导致浏览器可能自行使用了https://。这是一个
                加密的HTTP协议，导致浏览器读取乱码。
             */
            while((d = in.read())!=-1){
                System.out.print((char)d);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}







