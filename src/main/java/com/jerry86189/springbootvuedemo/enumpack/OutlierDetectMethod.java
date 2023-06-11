package com.jerry86189.springbootvuedemo.enumpack;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * ClassName: OutlierDetectMethod
 * Description: TODO
 * date: 2023/06/11 20:36
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
public enum OutlierDetectMethod implements IEnum<String> {
    Z_SCORE("z-score"),
    BOXPLOT("boxplot"),
    ELLIPTIC_ENVELOPE("elliptic envelope"),
    LOCAL_OUTLIER_FACTOR("local outlier factor"),
    IQR("iqr");

    @EnumValue
    private final String value;

    OutlierDetectMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
