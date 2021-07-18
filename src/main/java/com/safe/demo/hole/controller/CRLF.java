package com.safe.demo.hole.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * log4j可以预防crlf漏洞未知，未证明
 * crlf在header头处存在概率极低，现代浏览器大多已修复这个问题
 * crlf还和中间件的版本有关系，未能证明
 */
@Controller
public class CRLF {
    private Logger logger = LoggerFactory.getLogger(CRLF.class);

    @GetMapping("crlf")
    public void setCookie(HttpServletResponse response,String val) throws UnsupportedEncodingException {
        val = URLDecoder.decode(val, "UTF-8");
        logger.info("cookie:"+val.toString());
        Cookie cookie = new Cookie("key",val);
        cookie.setMaxAge(30);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }



    public static void main(String[] args) {
        String username = "1213\r\n23423";
        System.out.println(username);
        System.out.println("----------");
        //正则替换crlf字符
        Pattern p = Pattern.compile("%0a|%0d%0a|\n|\r\n");
        Matcher m = p.matcher(username);
        String dest = m.replaceAll("");
        System.out.println(dest);
        String phone = "15836182721";

        System.out.println(phone.replaceAll(phone.substring(3,7),"****"));
    }


}
