package com.jerry86189.springbootvuedemo.dto;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/17 20:56
 * @version 1.0
 */

import lombok.Data;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/17 20:56
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
