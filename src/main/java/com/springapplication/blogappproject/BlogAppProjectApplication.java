package com.springapplication.blogappproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class BlogAppProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAppProjectApplication.class, args);
    }

}
