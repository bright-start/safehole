package com.safe.demo.hole.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: white_xiaosheng
 * @Description: TODO
 * @CreateTime: 2021/3/7 3:28 下午
 * @Version 1.0
 */
@Api(tags="swagger控制器（自实现）")
@RestController
public class SwaggerRedirectController {
    @ApiOperation("springboot启动时默认跳转url")
    @ApiResponse(code = 200, message = "默认跳转到/swagger-ui.html页面")
    @GetMapping("/")
    public void swaggerRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }
}
