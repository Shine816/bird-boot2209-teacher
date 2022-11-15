package com.webserver.test;

import java.util.Arrays;

public class SplitDemo {
    public static void main(String[] args) {
        String line = "1=2=3=4=5=6=========";
        String[] data = line.split("=");
        System.out.println(Arrays.toString(data));
        //拆分出2项
        data = line.split("=",2);
        System.out.println(Arrays.toString(data));
        //拆分出3项
        data = line.split("=",3);
        System.out.println(Arrays.toString(data));
        //当最大可拆分项小于limit时，仅保留最多可拆分项(如果最后连续拆分空字符串也都会保留)
        data = line.split("=",100);
        System.out.println(Arrays.toString(data));

        //当limit为0时效果与split(String regex)一致。
        data = line.split("=",0);
        System.out.println(Arrays.toString(data));

        //当limit<0时，应拆尽拆
        data = line.split("=",-1);
        System.out.println(Arrays.toString(data));
    }
}
