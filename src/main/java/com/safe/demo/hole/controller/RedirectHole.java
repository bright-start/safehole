package com.safe.demo.hole.controller;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RedirectHole {
    /**
     *
     * jdk8,tomcat
     * crlf漏洞测试 location位置过滤%0d%0a querystring位置不过滤%0d%0a
     *
     * poc
     * /redirect?url=https://www.baidu.com
     *
     * 域名饶过
     * /redirect?url=https://localhost:8081@https:www.baidu.com
     *
     * 数字域名饶过
     * /redirect?url=https://localhost:8081@3702859413
     */
    @GetMapping("/redirect_attack")
    public void redirect_attack(HttpServletRequest request,HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }


    @GetMapping("/redirect_protect")
    public void redirect_protect(HttpServletRequest request,HttpServletResponse response, String url) throws IOException {
        String url2 = request.getContextPath()+(url.startsWith("/")?"":"/")+url;
        System.out.println(url2);
        response.sendRedirect(url2);
    }
}
