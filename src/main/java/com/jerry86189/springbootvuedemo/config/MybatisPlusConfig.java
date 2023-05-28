package com.jerry86189.springbootvuedemo.config;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/16 18:36
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @project SpringBootVueDemo
 * @author Jerry
 * @date 2023/5/16 18:36
 * @version 1.0
 * @description MybatisPlus配置类，主要用于配置MybatisPlus的拦截器
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
