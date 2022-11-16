package com.webserver.test;

/**
 * JAVA 23种设计模式之一:单例模式
 *
 * 使用单例模式设计的类，全局尽可以得到一个实例。
 * 1:私有化构造器.    目的:封堵外界可以通过new随意实例化对象
 * 2:定义静态的私有的当前类的属性并初始化(饿汉加载方式)
 * 3:提供公开的静态的获取当前类实例的方法
 */
public class Singleton {
    private static Singleton instance = new Singleton();

    private Singleton(){}

    public static Singleton getInstance(){
        return instance;
    }
}
