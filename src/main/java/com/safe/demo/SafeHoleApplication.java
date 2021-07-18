package com.safe.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@MapperScan({"com.safe.demo.hole.mapper","com.safe.demo.web.mapper"})
@EnableJpaRepositories
public class SafeHoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeHoleApplication.class, args);
	}


}