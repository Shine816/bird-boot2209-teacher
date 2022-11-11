package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示浏览器发送过来的一个HTTP请求
 * 一个请求由三部分构成的:
 * 1:请求行
 * 2:消息头
 * 3:消息正文
 */
public class HttpServletRequest {
    private Socket socket;
    //请求行的相关信息
    private String method;                              //请求方式
    private String uri;                                 //抽象路径
    private String protocol;                            //协议版本
    //消息头的相关信息
    private Map<String, String> headers = new HashMap<>();      //key:消息头名字 value:对应的值

    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        //1.1:读取请求行
        String line = readLine();
        System.out.println("请求行:" + line);
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];

        //1.2解析消息头
        while (true) {
            line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println("消息头:" + line);
            data = line.split(":\\s");
            headers.put(data[0].toLowerCase(), data[1]);
        }
        System.out.println("所有消息头:" + headers);
    }
    //解析请求行
    private void parseRequestLine(){}
    //解析消息头
    private void parseHeaders(){}
    //解析消息正文
    private void parseContent(){}

    /**
     * 读取客户端发送过来的一行字符串
     *
     * @return
     */
    private String readLine() throws IOException {//被重用的代码对应的方法通常不自己处理异常
        InputStream in = socket.getInputStream();
        StringBuilder builder = new StringBuilder();//保存拼接的一行内容
        char cur = 'a', pre = 'a';//cur记录本次读取的字符，pre记录上次读取的字符
        int d;//每次读取的字节
        while ((d = in.read()) != -1) {
            cur = (char) d;//将本次读取到的字节转换为char记录在cur上。
            if (pre == 13 && cur == 10) {//如果上次读取的为回车符，本次读取的是换行符
                break;//停止读取(一行结束了)
            }
            builder.append(cur);//将本次读取的字符拼接到已经读取的字符串中
            pre = cur;//在下次读取前将本次读取的字符记作"上次读取的字符"
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        /*
            因为headers中消息头名字以全小写形式保存，
            所以要将获取的消息头名字也转换为全小写再提取
            这样的好处就是不要求外界获取消息头时指定的名字必须区分大小写了
         */
        name = name.toLowerCase();
        return headers.get(name);
    }
}
