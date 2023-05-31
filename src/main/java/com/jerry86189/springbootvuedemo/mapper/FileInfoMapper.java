package com.jerry86189.springbootvuedemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerry86189.springbootvuedemo.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description TODO
 * @date 2023/5/30 14:22
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
}
