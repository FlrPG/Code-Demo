package com.lmzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.lmzz"})
public class ThreadDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThreadDemoApplication.class);
    }
}
