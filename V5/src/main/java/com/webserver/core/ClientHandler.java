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
            /*
                后期开发中常用的相对路径:类加载路径(保存这所有当前项目中class文件和包的根目录)
                在MAVEN项目中将我们的项目编译后就会把src/main/java中的内容全部编译并且
                和src/main/resources下的内容合并存放在target/classes中。
                因此target/classes目录就是类加载路径。

                定位类加载路径，固定写法:
                任何当前项目中的类的类名(通常在哪个类中需要访问类加载路径就用哪个类)
                类名.class.getClassLoader().getResource(".")
             */
            //类加载路径:target/classes
            File root = new File(
                ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            //定位target/classes/static目录(SpringBoot中存放所有静态资源的目录)
            File staticDir = new File(root,"static");
            //定位target/classes/static目录中的"index.html"
            File file = new File(staticDir,"index.html");


            //3发送响应
            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)
                (CRLF)
                1011101010101010101......
             */
            //3.1发送状态行
            println("HTTP/1.1 200 OK");

            //3.2发送响应头
            println("Content-Type: text/html");
            println("Content-Length: "+file.length());

            println("");//单独发送空行(回车+换行)表示响应头部分发送完毕


            //3.3发送响应正文(file表示的文件内容)
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
//            FileOutputStream fos = new FileOutputStream("xxx.xx");
            int len;//每次读取的字节量
            byte[] buf = new byte[1024*10];//10kb
            while((len = fis.read(buf))!=-1){
//                fos.write(buf,0,len);
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







