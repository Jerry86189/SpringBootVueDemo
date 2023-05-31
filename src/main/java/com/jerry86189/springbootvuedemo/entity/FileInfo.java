package com.jerry86189.springbootvuedemo.entity;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/30 14:21
 * @version 1.0
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/30 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_info")
public class FileInfo implements Serializable {
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    @TableField("file_name")
    private String fileName;

    @TableField("upload_timestamp")
    private Timestamp uploadTimestamp;

    @TableField("file_size")
    private Long fileSize;

    @TableField("file_path")
    private String filePath;
}
