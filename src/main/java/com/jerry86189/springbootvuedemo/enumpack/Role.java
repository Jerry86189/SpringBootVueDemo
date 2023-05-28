package com.jerry86189.springbootvuedemo.enumpack;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/16 11:49
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/16 11:49
 */
public enum Role implements IEnum<String> {
    ADMIN("ADMIN"),
    NORM("NORM");

    @EnumValue
    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
