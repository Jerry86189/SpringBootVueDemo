package com.jerry86189.springbootvuedemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jerry86189.springbootvuedemo.enumpack.OutlierDeleteMethod;
import com.jerry86189.springbootvuedemo.enumpack.OutlierDetectMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * ClassName: OutlierMsg
 * Description: TODO
 * date: 2023/06/11 18:10
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@TableName("outlier_msg")
public class OutlierMsg implements Serializable {
    @TableId(value = "outlier_id", type = IdType.AUTO)
    private Long outlierId;

    @TableField("cols_json")
    private String colsJson;

    @TableField("delete_method")
    private OutlierDeleteMethod deleteMethod;

    @TableField("threshold")
    private Integer threshold;

    @TableField("type")
    private Boolean type;

    @TableField("replace_value")
    private Float replaceValue;

    @TableField("detect_method")
    private OutlierDetectMethod detectMethod;

    @TableField(exist = false)
    private List<String> cols;

    public void setCols(List<String> cols) {
        this.cols = cols;
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.colsJson = mapper.writeValueAsString(cols);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCols() {
        if (this.cols == null && this.colsJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.cols = mapper.readValue(this.colsJson, new TypeReference<List<String>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.cols;
    }
}
