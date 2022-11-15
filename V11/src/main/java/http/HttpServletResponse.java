package http;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示一个HTTP的响应
 * 一个响应由三部分构成:
 * 状态行，响应头，响应正文
 *
 */
public class HttpServletResponse {
    private static MimetypesFileTypeMap mft = new MimetypesFileTypeMap();

    private Socket socket;
    //状态行相关信息
    private int statusCode = 200;                           //状态代码
    private String statusReason = "OK";                     //状态描述
    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();   //key:响应头名字 value:对应的值
    //响应正文相关信息
    private File contentFile;                               //响应正文对应的实体文件


    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /**
     * 用于将当前响应对象的内容以标准的HTTP响应格式发送给客户端(浏览器)
     */
    public void response() throws IOException {
        //3.1发送状态行
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文
        sendContent();
    }

    private void sendStatusLine() throws IOException {
        println("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }
    private void sendHeaders() throws IOException {
        /*
            header
            key             value
            Content-Type    text/html   Entry
            Content-Length  245         Entry
            Server          WebServer   Entry
            XXX             XXXX        Entry
         */
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for(Map.Entry<String,String> e : entrySet){
            String key = e.getKey();
            String value = e.getValue();
            println(key + ": " + value);
        }

        println("");
    }
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        int len;
        byte[] buf = new byte[1024*10];
        while((len = fis.read(buf))!=-1){
            out.write(buf,0,len);
        }
    }


    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//发送回车符
        out.write(10);//发送换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
        addHeader("Content-Type",mft.getContentType(contentFile));
        addHeader("Content-Length",contentFile.length()+"");
    }

    /**
     * 添加一个响应头
     * @param name  响应头的名字
     * @param value 响应头对应的值
     */
    public void addHeader(String name,String value){
        headers.put(name,value);
    }
}
