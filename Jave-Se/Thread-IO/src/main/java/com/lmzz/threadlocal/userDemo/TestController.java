package com.lmzz.threadlocal.userDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/test")
    public void test() {
        System.out.println("TestController。。。");
        userService.testMethod();
    }
}
