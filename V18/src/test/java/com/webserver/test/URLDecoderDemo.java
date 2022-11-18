package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String line = "/regUser?username=%E8%8C%83%E4%BC%A0%E5%A5%87&password=123456&nickname=%E4%BC%A0%E5%A5%87&age=22";

        line = URLDecoder.decode(line,"UTF-8");
        System.out.println(line);

    }
}
