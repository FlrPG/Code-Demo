package com.flr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisDistributedLockApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(RedisDistributedLockApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}