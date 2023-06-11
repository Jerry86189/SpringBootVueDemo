package com.jerry86189.springbootvuedemo.enumpack;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * ClassName: OutlierDeleteMethod
 * Description: TODO
 * date: 2023/06/11 20:54
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
public enum OutlierDeleteMethod implements IEnum<String> {
    MEAN_STD("mean_std"),
    BOXPLOT("boxplot"),
    CLUSTER("cluster");

    @EnumValue
    private final String value;

    OutlierDeleteMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
