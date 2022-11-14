package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

/**
 * DispatcherServlet实际是由SpringMVC框架提供的一个类，用于和Tomcat整合并负责
 * 接手处理请求的工作。
 *
 * Servlet是JAVA EE里的一个接口，译作:运行在服务端的小程序
 * Servlet中有一个重要的抽象方法:
 * public void service(HttpServletRequest request,HttpServletResponse response)
 * 该方法用于处理某个服务
 *
 * SpringMVC框架提供的DispatcherServlet就实现了该接口并重写了service方法，那么与Tomcat整合后，Tomcat在处理
 * 请求的环节就可以调用DispatcherServlet的service方法将请求对象与响应对象传递进去由SpringMVC框架完成处理请求
 * 的操作。
 */
public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
    private static File root;
    private static File staticDir;
    static {
        try {
            root = new File(
                DispatcherServlet.class.getClassLoader().getResource(".").toURI()
            );
            staticDir = new File(root,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet(){}
    public static DispatcherServlet getInstance(){
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response){
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
    }
}
