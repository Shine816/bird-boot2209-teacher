package com.webserver.test;

import javax.activation.MimetypesFileTypeMap;

/**
 * 测试根据文件获取对应的MIME类型
 */
public class ContentTypeDemo {
    public static void main(String[] args) {
        //该类内部初始化时要读取
        MimetypesFileTypeMap mft = new MimetypesFileTypeMap();
    }
}
