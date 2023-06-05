package com.jerry86189.springbootvuedemo.config;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/6/2 19:37
 * @version 1.0
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/6/2 19:37
 */
@Configuration
public class ClassConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
