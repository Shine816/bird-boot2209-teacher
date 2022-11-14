package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
            File root = new File(
                ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            File staticDir = new File(root,"static");

            String path = request.getUri();
            File file = new File(staticDir,path);

            if(file.isFile()){//判断请求的文件真实存在且确定是一个文件(不是目录)
                response.setContentFile(file);

            }else{//404情况
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir,"404.html");
                response.setContentFile(file);
            }

            //3发送响应
            response.response();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
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







