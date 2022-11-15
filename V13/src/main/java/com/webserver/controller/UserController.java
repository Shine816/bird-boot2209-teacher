package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 用于处理用户相关业务
 */
public class UserController {
    private static File root;
    private static File staticDir;
    static {
        try {
            root = new File(
                    UserController.class.
                            getClassLoader().getResource(".").toURI());
            staticDir = new File(root,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户注册!");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);
        //必要验证工作
        if(username==null||username.isEmpty()||password==null||password.isEmpty()||
                nickname==null||nickname.isEmpty()||ageStr==null||ageStr.isEmpty()||
                !ageStr.matches("[0-9]+")){
            //要求浏览器查看错误提示页面
           File file = new File(staticDir,"/reg_info_error.html");
           response.setContentFile(file);

        }

    }
}
