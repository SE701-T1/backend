package com.team701.buddymatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
public class BuddyMatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuddyMatcherApplication.class, args);
    }


}

