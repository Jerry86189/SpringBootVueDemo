package com.jerry86189.springbootvuedemo.enumpack;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * ClassName: MissingMethod
 * Description: TODO
 * date: 2023/06/11 20:07
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
public enum MissingMethod implements IEnum<String> {
    MEAN("mean"),
    MEDIAN("median"),
    MODE("mode"),
    KNN("knn"),
    RF("rf"),
    LR("lr");

    @EnumValue
    private final String value;

    MissingMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
