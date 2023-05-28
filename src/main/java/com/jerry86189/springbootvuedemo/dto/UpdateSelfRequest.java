package com.jerry86189.springbootvuedemo.dto;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/24 23:54
 * @version 1.0
 */

import com.jerry86189.springbootvuedemo.entity.User;
import lombok.Data;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/24 23:54
 */
@Data
public class UpdateSelfRequest {
    private Long verifyId;
    private String verifyPassword;
    private User user;
}
