package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
            //1.1:读取请求行
            String line = readLine();
            System.out.println("请求行:"+line);
            //将请求行内容按照空格拆分为三部分，并赋值给下面三个变量
            String[]data = line.split("\\s");
            String method = data[0];//请求方式
            String uri = data[1];//抽象路径
            String protocol = data[2];//协议版本
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

            //1.2解析消息头
            Map<String,String> headers = new HashMap<>();
            while(true) {
                line = readLine();
                if(line.isEmpty()){//若读取到了空行，则说明消息头部分读取完毕
                    break;
                }
                System.out.println("消息头:" + line);
                //每个消息头都按照": "(冒号空格)拆分为消息头的名字和值并以key,value存入headers
                data = line.split(":\\s");
                /*
                    将消息头存入Map时，消息头的名字转换为全小写，便于后期获取消息头
                    (获取时不用在考虑大小写问题)
                 */
                headers.put(data[0].toLowerCase(),data[1]);
            }
            System.out.println("所有消息头:"+headers);


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

    /**
     * 读取客户端发送过来的一行字符串
     * @return
     */
    private String readLine() throws IOException {//被重用的代码对应的方法通常不自己处理异常
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
        return builder.toString().trim();
    }

}







