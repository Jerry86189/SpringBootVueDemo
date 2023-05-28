package com.jerry86189.springbootvuedemo.controller;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/19 11:17
 * @version 1.0
 */

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @project SpringBootVueDemo
 * @author Jerry
 * @date 2023/5/19 11:17
 * @version 1.0
 * @description 首页控制器类，主要用于处理网站首页的请求
 */
@Controller
public class IndexController {
    /**
     * 提供用户访问首页的接口
     * @return 重定向到登录页面
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String index() {
        System.out.println("index page");
        return "forward:/login.html";
    }
}
