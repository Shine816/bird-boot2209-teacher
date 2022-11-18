package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * DispatcherServlet实际是由SpringMVC框架提供的一个类，用于和Tomcat整合并负责
 * 接手处理请求的工作。
 * <p>
 * Servlet是JAVA EE里的一个接口，译作:运行在服务端的小程序
 * Servlet中有一个重要的抽象方法:
 * public void service(HttpServletRequest request,HttpServletResponse response)
 * 该方法用于处理某个服务
 * <p>
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
            staticDir = new File(root, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {
    }

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        //判断用户的请求路径不应当含有参数部分。所以uri不适用。
        String path = request.getRequestURI();

        //判断是否为请求业务
        Method method = HandlerMapping.getMethod(path);
        if(method!=null){
            try {
                //通过方法对象可以获取到该方法所属的类的类对象
                Object obj = method.getDeclaringClass().newInstance();
                method.invoke(obj,request,response);
            } catch (Exception e) {
                //若调用某个Controller的方法时出现了异常应当回复浏览器500错误
                response.setStatusCode(500);
                response.setStatusReason("Internal Server Error");
                response.setContentFile(new File(staticDir,"500.html"));
            }
        }else {
            File file = new File(staticDir, path);
            if (file.isFile()) {//判断请求的文件真实存在且确定是一个文件(不是目录)
                response.setContentFile(file);
            } else {//404情况
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "404.html");
                response.setContentFile(file);
            }
        }
        response.addHeader("Server", "BirdServer");
    }
}
