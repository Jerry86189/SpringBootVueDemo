package com.jerry86189.springbootvuedemo.dto;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/24 22:16
 * @version 1.0
 */

import lombok.Data;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/24 22:16
 */
@Data
public class DeleteSelfRequest {
    private String username;
    private String password;
}
