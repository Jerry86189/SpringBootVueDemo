package com.jerry86189.springbootvuedemo.entity;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/15 21:36
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jerry86189.springbootvuedemo.enumpack.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/15 21:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User implements Serializable {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField(value = "role")
    private Role role;
}
