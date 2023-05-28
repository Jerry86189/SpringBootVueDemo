package com.jerry86189.springbootvuedemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jerry86189.springbootvuedemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description TODO
 * @date 2023/5/16 12:12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
