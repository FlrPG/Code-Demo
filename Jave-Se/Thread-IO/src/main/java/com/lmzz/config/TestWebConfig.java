package com.lmzz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TestWebConfig implements WebMvcConfigurer {

    @Bean
    public InterceptorDemo getBean() {
        return new InterceptorDemo();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getBean()).addPathPatterns("/**");

    }
}
