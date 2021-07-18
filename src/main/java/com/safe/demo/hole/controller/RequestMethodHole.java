package com.safe.demo.hole.controller;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: white_xiaosheng
 * @Description: TODO
 * @CreateTime: 2021/7/2 3:12 下午
 * @Version 1.0
 */
@RestController
public class RequestMethodHole {

    @RequestMapping(value = "/track_attack",method = RequestMethod.GET)
    public String trackAttack(HttpServletRequest request){
        return "ok";
    }
}
