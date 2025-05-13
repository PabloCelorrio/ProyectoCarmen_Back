package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication

@ConfigurationProperties
@ComponentScan(basePackages = {"com.example"})
public class Main {
    private static String port;

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Main.class, args);
        Environment environment = context.getEnvironment();
        port = environment.getProperty("server.port");

    }

}