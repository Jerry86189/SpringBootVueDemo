package com.jerry86189.springbootvuedemo.enumpack;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * ClassName: OutlierReplaceMethod
 * Description: TODO
 * date: 2023/06/11 20:24
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
public enum OutlierReplaceMethod implements IEnum<String> {
    MEAN("mean"),
    MEDIAN("median"),
    MODE("mode"),
    CONSTANT("constant"),
    RANDOM("random"),
    MODEL("model");

    @EnumValue
    private final String value;

    OutlierReplaceMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
