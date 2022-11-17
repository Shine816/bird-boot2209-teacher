package com.webserver.core;

import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

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
        /*
            当我们得到本次请求路径path的值后，我们首先要查看是否为请求业务:
            1:扫描controller包下的所有类
            2:查看哪些被注解@Controller标注的过的类(只有被该注解标注的类才认可为业务处理类)
            3:遍历这些类，并获取他们的所有方法，并查看哪些时业务方法
              只有被注解@RequestMapping标注的方法才是业务方法
            4:遍历业务方法时比对该方法上@RequestMapping中传递的参数值是否与本次请求
              路径path值一致?如果一致则说明本次请求就应当由该方法进行处理
              因此利用反射机制调用该方法进行处理。
            5:如果扫描了所有的Controller中所有的业务方法，均未找到与本次请求匹配的路径
              则说明本次请求并非处理业务，那么执行下面请求静态资源的操作
         */
        try {
            File dir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource("./com/webserver/controller").toURI()
            );

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File file = new File(staticDir, path);
        if (file.isFile()) {//判断请求的文件真实存在且确定是一个文件(不是目录)
            response.setContentFile(file);
        } else {//404情况
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "404.html");
            response.setContentFile(file);
        }
        response.addHeader("Server", "BirdServer");
    }
}
