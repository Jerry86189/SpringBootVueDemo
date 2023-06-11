package com.jerry86189.springbootvuedemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerry86189.springbootvuedemo.enumpack.MissingMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * ClassName: DeleteMsg
 * Description: TODO
 * date: 2023/06/11 18:03
 *
 * @author Jerry
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@TableName("missing_msg")
public class MissingMsg implements Serializable {
    @TableId(value = "missing_id", type = IdType.AUTO)
    private Long missingId;

    @TableField("cols_json")
    private String columnsJson;

    @TableField("type")
    private Boolean type;

    @TableField("missing_method")
    private MissingMethod missingMethod;

    @TableField(exist = false)
    private List<String> columns;

    public void setColumns(List<String> columns) {
        this.columns = columns;
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.columnsJson = mapper.writeValueAsString(columns);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<String> getColumns() {
        if (this.columns == null && this.columnsJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.columns = mapper.readValue(this.columnsJson, new TypeReference<List<String>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.columns;
    }
}
