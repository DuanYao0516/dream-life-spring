package com.example.hellospring;

import com.example.hellospring.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.example.hellospring.mapper")
public class HellospringApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HellospringApplication.class);
        application.run(args);
    }

}
