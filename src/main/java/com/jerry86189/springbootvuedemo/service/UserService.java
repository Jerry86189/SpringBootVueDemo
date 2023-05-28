package com.jerry86189.springbootvuedemo.service;

import com.jerry86189.springbootvuedemo.dto.LoginResponse;
import com.jerry86189.springbootvuedemo.entity.User;
import com.jerry86189.springbootvuedemo.exceptions.LoginFailedException;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description 用户服务接口
 * @date 2023/5/16 12:22
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，如果用户名或密码错误，则抛出异常
     */
    LoginResponse login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 待注册的用户信息
     * @return 注册成功的用户信息
     */
    User register(User user);

    /**
     * 删除用户账户
     *
     * @param username 用户名
     * @param password 密码
     */
    void deleteAccount(String username, String password);

    /**
     * 更新用户信息
     *
     * @param userId   用户id
     * @param password 原密码
     * @param user     新的用户信息
     * @return 更新后的用户信息
     */
    User updateUserInfo(Long userId, String password, User user);

    /**
     * 分页获取所有用户信息，仅管理员可操作
     *
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 用户信息列表
     */
    List<User> getAllUsersByPage(int pageNum, int pageSize);//因为查询用户、编辑用户和删除用户是在同一个界面上面的，然而直接使用这种方法的话会造成一个管理员可以随意增删改查其他管理员的情况出现，所以要再次的基础上新增一个获取所有普通用户的东西

    /**
     * 根据用户id获取用户信息，仅管理员可操作
     *
     * @param id 用户id
     * @return 查询到的用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户名获取用户信息，仅管理员可操作
     *
     * @param username 用户名
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 查询到的用户信息列表
     */
    List<User> getUserByUsername(String username, int pageNum, int pageSize);

    /**
     * 根据用户id删除普通用户账户，仅管理员可操作
     *
     * @param id 用户id
     */
    void deleteNormUserById(Long id);

    /**
     * 获取用户总数，仅管理员可操作
     *
     * @return 用户总数
     */
    int getCountOfUsers();

    /**
     * 分页获取所有普通用户信息，仅管理员可操作
     *
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 普通用户信息列表
     */
    List<User> getAllNormUsersByPage(int pageNum, int pageSize);

    /**
     * 根据用户id获取普通用户信息，仅管理员可操作
     *
     * @param id 用户id
     * @return 查询到的普通用户信息
     */
    User getNormUserById(Long id);

    /**
     * 根据用户名获取普通用户信息，仅管理员可操作
     *
     * @param username 用户名
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 查询到的普通用户信息列表
     */
    List<User> getNormUserByUsername(String username, int pageNum, int pageSize);

    /**
     * 根据用户名模糊查询用户总数，仅管理员可操作
     *
     * @param username 用户名
     * @return 查询到的用户总数
     */
    int getCountOfUsersByUsername(String username);

    /**
     * 获取普通用户总数，仅管理员可操作
     *
     * @return 普通用户总数
     */
    int getCountOfNormUsers();

    /**
     * 根据用户名获取普通用户总数，仅管理员可操作
     *
     * @param username 用户名
     * @return 指定用户名的普通用户总数
     */
    int getCountOfNormUsersByUsername(String username);
}
