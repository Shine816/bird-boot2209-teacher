package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于维护所有请求路径与对应的业务处理类Controller的处理方法
 */
public class HandlerMapping {
    /*
        key:请求路径(方法上的注解@RequestMapping中的参数值)
        value:处理该请求的方法(某Controller的一个方法)
     */
    private static Map<String, Method> mapping = new HashMap<>();

    static{
        initMapping();
    }

    private static void initMapping(){
        try {
            /*
                定位引入当前BirdBoot项目类的启动类所在的包
                实际上SpringBoot的约定时:提供的所有Controller所在包至少要放到启动类所在的包里
             */
            File root = new File(
                    BirdBootApplication.primarySource.getResource(".").toURI()
            );
            //定位启动类所在目录下的controller目录(所有Controller类都应当在这里)
            File dir = new File(root,"controller");
            if(!dir.exists()){//controller目录不存在则不进行初始化
                return;
            }
            File[] subs = dir.listFiles(f->f.getName().endsWith(".class"));
            for(File sub : subs){
                String fileName = sub.getName();
                String className = fileName.substring(0,fileName.indexOf("."));
                //由于约定所有Controller类都要在名为controller的包中，而这个包必须与启动类在同一个包里
                String packageName = BirdBootApplication.primarySource.getPackage().getName();
                Class cls = Class.forName(packageName+".controller."+className);
                //是否为@Controller标注的类
                if(cls.isAnnotationPresent(Controller.class)){
                    Method[] methods = cls.getDeclaredMethods();
                    for(Method method : methods){
                        //是否该方法被@RequestMapping标注
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String value = rm.value();
                            //将注解参数作为key,该方法对象作为value存储
                            mapping.put(value,method);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据给定的请求路径获取对应的处理方法
     * @param path  应当与某个Controller中的业务方法上@RequestMapping注解值一致
     * @return  与path匹配的处理方法或null
     */
    public static Method getMethod(String path){
        return mapping.get(path);
    }

    public static void main(String[] args) {
        Method m = mapping.get("/regUser");
        System.out.println(m);
    }

}
