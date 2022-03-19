package com.team701.buddymatcher;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BuddyMatcherApplication {


    @Value( "${socketio.host}" )
    private String socketIOHost;

    @Value( "${socketio.port}" )
    private Integer socketIOPort;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(socketIOHost);
        config.setPort(socketIOPort);
        return new SocketIOServer(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(BuddyMatcherApplication.class, args);
    }

}

