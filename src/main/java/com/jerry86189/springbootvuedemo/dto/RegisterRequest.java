package com.jerry86189.springbootvuedemo.dto;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/17 22:22
 * @version 1.0
 */

import com.jerry86189.springbootvuedemo.entity.User;
import lombok.Data;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/17 22:22
 */
@Data
public class RegisterRequest {
    private User user;
    private String otherAdminUsername;
    private String otherAdminPassword;
}
