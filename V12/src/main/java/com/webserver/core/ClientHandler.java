package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;

/**
 * 该线程任务负责与指定的客户端进行HTTP交互
 * 按照HTTP协议的交互规则，与浏览器采取一问一答。
 * 规划三部:
 * 1:解析请求(生成HttpServletRequest对象)
 * 2:处理请求(交给SpringMVC框架处理)
 * 3:发送响应(将HttpServletResponse内容发送给浏览器)
 * 断开TCP连接
 *
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            //1解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            //2处理请求
            DispatcherServlet servlet = DispatcherServlet.getInstance();
            servlet.service(request,response);

            //3发送响应
            response.response();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {

        } finally {
            try {
                //一问一答后断开TCP连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}







