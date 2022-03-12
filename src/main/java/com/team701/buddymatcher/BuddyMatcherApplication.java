package com.team701.buddymatcher;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableWebMvc
public class BuddyMatcherApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(BuddyMatcherApplication.class, args);
    }


}

