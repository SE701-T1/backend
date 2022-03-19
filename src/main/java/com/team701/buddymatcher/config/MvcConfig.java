package com.team701.buddymatcher.config;

import com.team701.buddymatcher.interceptor.UserInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan("com.team701.buddymatcher.controllers")
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // Pre-handle all requests except those which go to the login endpoint
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/**");
    }
}
