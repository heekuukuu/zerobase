package com.example.Account.config;//package com.example.Account.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import redis.embedded.RedisServer;
//
//
//@Configuration
//
//public class LocalRedisConfig {
//    @Value("${spring.redis.port}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void startRedis() {
//        try {
//            redisServer = new RedisServer(redisPort);
//            redisServer.start();
//            System.out.println("Embedded Redis started on port: " + redisPort);
//        } catch (Exception e) {
//            System.err.println("Failed to start embedded Redis: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if (redisServer != null) {
//            redisServer.stop();
//            System.out.println("Embedded Redis stopped.");
//        }
//    }
//}
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class LocalRedisConfig {

    private RedisServer redisServer;

    @Bean
    public RedisServer redisServer() throws IOException {
        redisServer = new RedisServer(6379); // 포트 설정
        redisServer.start();
        return redisServer;
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}