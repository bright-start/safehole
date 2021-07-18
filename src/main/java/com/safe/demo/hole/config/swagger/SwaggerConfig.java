package com.safe.demo.hole.config.swagger;

import io.swagger.models.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: white_xiaosheng
 * @Description: swagger文档基本配置类
 * @CreateTime: 2021/2/24 9:43 上午
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * @Description:swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.safe.demo.hole.controller"))
                .paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(false);
    }

    /**
     * @Description: 构建 api文档的信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置页面标题
                .title("使用swagger2构建后端api接口文档")
                // 设置联系人
                .contact("123@qq.com")
                // 描述
                .description("欢迎访问接口文档，这里是描述信息")
                // 定义版本号
                .version("1.0").build();
    }
}
