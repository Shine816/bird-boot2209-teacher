package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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

    private String requestURI;                          //uri中请求部分("?"左侧内容)
    private String queryString;                         //uri中参数部分("?"右侧内容)
    private Map<String,String> parameters = new HashMap<>();//存每一组参数

    //消息头的相关信息
    private Map<String, String> headers = new HashMap<>();      //key:消息头名字 value:对应的值

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        //1.1:读取请求行
        parseRequestLine();
        //1.2解析消息头
        parseHeaders();
        //1.3解析消息正文
        parseContent();

    }
    //解析请求行
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();
        if(line.isEmpty()){//如果请求行没有读取到内容，则为空请求
            throw new EmptyRequestException();
        }
        System.out.println("请求行:" + line);
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];
        parseURI();//进一步解析uri
    }
    //进一步解析uri(将请求行中的抽象路径部分进一步解析)
    private void parseURI(){
        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1){
            queryString = data[1];
            parseParameter(queryString);
        }
    }

    //解析参数
    private void parseParameter(String line){
        //为参数部分转码
        try {
            line = URLDecoder.decode(line,"UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        String[] paraArray = line.split("&");
        for(String para : paraArray){
            String[] paras = para.split("=",2);
            parameters.put(paras[0], paras[1]);
        }
    }

    //解析消息头
    private void parseHeaders() throws IOException {
        while (true) {
            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println("消息头:" + line);
            String[] data = line.split(":\\s");
            headers.put(data[0].toLowerCase(), data[1]);
        }
        System.out.println("所有消息头:" + headers);
    }
    //解析消息正文
    private void parseContent() throws IOException {
        /*
            首先:判断请求中是否含有消息头Content-Length
                 因为这个头是浏览器告知本次请求中消息正文的长度。
                 如果不含有这个头说明本次请求没有正文。
         */
        String contentLength = getHeader("Content-Length");
        if(contentLength!=null){
            int length = Integer.parseInt(contentLength);
            byte[] data = new byte[length];
            InputStream in = socket.getInputStream();
            in.read(data);//将消息正文所有字节读入数组

            //根据消息头Content-Type判定正文类型并对应转换
            String contentType = getHeader("Content-Type");
            //判断是否为不含有附件的普通表单内容
            if("application/x-www-form-urlencoded".equals(contentType)){
                //正文就是一行字符串，内容是原GET形式提交后抽象路径中"?"后面内容
                String line = new String(data, StandardCharsets.ISO_8859_1);
                System.out.println("正文内容:"+line);
                parseParameter(line);
            }
//            后期可添加其他判断，比如含有附件的正文解析
//            else if(){
//            }
        }
    }

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

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}
