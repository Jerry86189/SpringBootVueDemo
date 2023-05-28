package com.jerry86189.springbootvuedemo.dto;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/25 9:26
 * @version 1.0
 */

import com.jerry86189.springbootvuedemo.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/25 9:26
 */
@Data
public class GetUsersResponse {
    private List<User> users;
    private int currentPage;
    private int pageSize;
    private int totalCount;
    private int totalPages;
}
