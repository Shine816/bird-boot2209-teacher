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

                如果控制台只输出一个客户端连接，没有请求内容，通常是因为浏览器使用
                缓存而没有真实发送请求内容。
                解决办法:
                1:更换浏览器测试
                2:浏览器尽量不要用"后退，前进"这个功能测试，而是使用刷新访问。
             */
            while((d = in.read())!=-1){
                System.out.print((char)d);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}







