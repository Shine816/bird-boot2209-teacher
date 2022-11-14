package com.webserver.core;

import com.webserver.http.HttpServletRequest;

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

            //2处理请求
            File root = new File(
                ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            File staticDir = new File(root,"static");
            /*
                http://localhost:8088/index.html
                http://localhost:8088/classtable.html

                http://localhost:8088/index123.html     //static下没有index123.html
                http://localhost:8088                   //抽象路径:"/".这时File file = new File(staticDir,path)行会定位到static目录本身
                上述两种情况都会导致发送响应正文时使用文件输入流读取时报错
                1：不存在的文件会出现系统找不到指定的文件
                2：读取的是目录会导致出现无法访问
             */
            String path = request.getUri();
            File file = new File(staticDir,path);

            int statusCode;//状态代码
            String statusReason;//状态描述
            if(file.isFile()){//判断请求的文件真实存在且确定是一个文件(不是目录)
                statusCode = 200;
                statusReason = "OK";
            }else{//404情况
                statusCode = 404;
                statusReason = "NotFound";
                file = new File(staticDir,"404.html");
            }

            //3发送响应
            //3.1发送状态行
            println("HTTP/1.1" + " " + statusCode + " " + statusReason);

            //3.2发送响应头
            println("Content-Type: text/html");
            println("Content-Length: "+file.length());
            println("");


            //3.3发送响应正文(file表示的文件内容)
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            int len;
            byte[] buf = new byte[1024*10];
            while((len = fis.read(buf))!=-1){
                out.write(buf,0,len);
            }


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

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//发送回车符
        out.write(10);//发送换行符
    }



}







