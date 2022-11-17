package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

/**
 * 用于处理用户相关业务
 */
@Controller
public class UserController {
    private static File userDir;
    static {
        userDir = new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }
    }

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户注册!!!!!!!!!!");
        //对应的是reg.html页面上<input name="username">
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        //必要验证工作
        if(username==null||username.isEmpty()||password==null||password.isEmpty()||
                nickname==null||nickname.isEmpty()||ageStr==null||ageStr.isEmpty()||
                !ageStr.matches("[0-9]+")){
            //要求浏览器查看错误提示页面
            response.sendRedirect("/reg_info_error.html");
            return;

        }
        System.out.println(username+","+password+","+nickname+","+ageStr);

        int age = Integer.parseInt(ageStr);
        //2
        //在userDir表示的目录中创建指定名字的文件
        File file = new File(userDir,username+".obj");
        //检查是否为重复用户
        if(file.exists()){//若该文件已经存在了说明是已经注册过的用户了
            response.sendRedirect("/have_user.html");
            return;
        }

        User user = new User(username,password,nickname,age);
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
            //使用响应对象要求浏览器查看注册成功页面
            response.sendRedirect("/reg_success.html");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
