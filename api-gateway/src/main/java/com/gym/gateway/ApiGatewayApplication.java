package com.gym.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.gym.gateway.config.GatewayConfig;

@SpringBootApplication
@EnableConfigurationProperties(GatewayConfig.class)
public class ApiGatewayApplication { //a
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
