package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
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
            //1.1:读取请求行
            //测试读取一行字符串的操作
            InputStream in = socket.getInputStream();
            StringBuilder builder = new StringBuilder();//保存拼接的一行内容
            char cur='a',pre='a';//cur记录本次读取的字符，pre记录上次读取的字符
            int d;//每次读取的字节
            while((d = in.read())!=-1){
                cur = (char)d;//将本次读取到的字节转换为char记录在cur上。
                if(pre==13 && cur==10){//如果上次读取的为回车符，本次读取的是换行符
                    break;//停止读取(一行结束了)
                }
                builder.append(cur);//将本次读取的字符拼接到已经读取的字符串中
                pre = cur;//在下次读取前将本次读取的字符记作"上次读取的字符"
            }
            String line = builder.toString().trim();
            System.out.println("请求行:"+line);



            //2处理请求


            //3发送响应


        } catch (IOException e) {
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







