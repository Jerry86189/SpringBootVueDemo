package com.jerry86189.springbootvuedemo.service.impl;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/16 12:22
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jerry86189.springbootvuedemo.dto.LoginResponse;
import com.jerry86189.springbootvuedemo.entity.User;
import com.jerry86189.springbootvuedemo.exceptions.*;
import com.jerry86189.springbootvuedemo.mapper.UserMapper;
import com.jerry86189.springbootvuedemo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/16 12:22
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private String secretKey = "a9f7c4d8e3b6f1a2c5e8d7b4f6a3c9e7d8b6f4a2c7e9d6b3f5a8c4e6d9b7f3a5";

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，如果用户名或密码错误，则抛出异常
     */
    @Override
    public LoginResponse login(String username, String password) {
        System.out.println("enter UserServiceImpl login");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null || !new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new LoginFailedException("Your account" + username + "log in failed");
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(generateToken(user));
        System.out.println("UserServiceImpl login role: " + user.getRole().getValue());
        loginResponse.setUser(user);
        return loginResponse;
    }

    /**
     * 生成JWT令牌
     *
     * @param user 用户信息
     * @return JWT令牌
     */
    private String generateToken(User user) {
        if (user == null || user.getRole() == null) {
            return null;
        }
        String jws = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().getValue())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
        return jws;
    }

    /**
     * 用户注册
     *
     * @param user 待注册的用户信息
     * @return 注册成功的用户信息
     */
    @Override
    public User register(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new RegisterFailedException("Your account" + user.getUsername() + "register failed");
        }
        return user;
    }

    /**
     * 删除用户账户
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public void deleteAccount(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new DeleteFailedException("No user found with username: " + username);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, user.getPassword())) {
            userMapper.deleteById(user.getUserId());
        } else {
            throw new DeleteFailedException("Invalid password for user: " + username);
        }
    }

    /**
     * 更新用户信息
     *
     * @param userId   用户id
     * @param oldPassword 原密码
     * @param user     新的用户信息
     * @return 更新后的用户信息
     */
    @Override
    public User updateUserInfo(Long userId, String oldPassword, User user) {
        // 通过用户ID找到原始用户信息
        User originalUser = userMapper.selectById(userId);
        if (originalUser == null) {
            throw new UpdateFailedException("User with ID: " + userId + " not found");
        }

        // 检查原密码是否匹配
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, originalUser.getPassword())) {
            throw new UpdateFailedException("Password incorrect for user: " + userId);
        }

        // 更新用户信息
        String newPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        int update = userMapper.updateById(user);
        if (update <= 0) {
            throw new UpdateFailedException("Update account: " + userId + " failed");
        }

        return user;
    }

    /**
     * 分页获取所有用户信息，仅管理员可操作
     *
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 用户信息列表
     */
    @Override
    public List<User> getAllUsersByPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);

        if (userPage == null || userPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        return userPage.getRecords();
    }

    /**
     * 根据用户id获取用户信息，仅管理员可操作
     *
     * @param id 用户id
     * @return 查询到的用户信息
     */
    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);

        if (user == null) {
            throw new NotFoundException("Your account: " + id + " not found");
        }

        return user;
    }

    /**
     * 根据用户名获取用户信息，仅管理员可操作
     *
     * @param username 用户名
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 查询到的用户信息列表
     */
    @Override
    public List<User> getUserByUsername(String username, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);

        if (userPage == null || userPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        if (userPage.getRecords().isEmpty()) {
            throw new NotFoundException("No users found with username: " + username);
        }

        return userPage.getRecords();
    }

    /**
     * 根据用户id删除用户账户，仅管理员可操作
     *
     * @param id 用户id
     */
    @Override
    public void deleteNormUserById(Long id) {
        int delete = userMapper.deleteById(id);
        if (delete <= 0) {
            throw new DeleteFailedException("Your account: " + id + "delete failed");
        }
    }

    /**
     * 获取用户总数，仅管理员可操作
     *
     * @return 用户总数
     */
    @Override
    public int getCountOfUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectCount(queryWrapper);
    }

    /**
     * 分页获取所有普通用户信息，仅管理员可操作
     *
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 普通用户信息列表
     */
    @Override
    public List<User> getAllNormUsersByPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "NORM");
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);

        if (userPage == null || userPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        if (userPage.getRecords().isEmpty()) {
            throw new NotFoundException("No users found with Norm User");
        }

        return userPage.getRecords();
    }

    /**
     * 根据用户id获取普通用户信息，仅管理员可操作
     *
     * @param id 用户id
     * @return 查询到的普通用户信息
     */
    @Override
    public User getNormUserById(Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "NORM").eq("user_id", id);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new NotFoundException("User with id: " + id + " not found or is not a normal user");
        }

        return user;
    }

    /**
     * 根据用户名获取普通用户信息，仅管理员可操作
     *
     * @param username 用户名
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 查询到的普通用户信息列表
     */
    @Override
    public List<User> getNormUserByUsername(String username, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);

        if (userPage == null || userPage.getRecords() == null) {
            throw new PageQueryFailedException("Query page: " + pageNum + " failed");
        }

        if (userPage.getRecords().isEmpty()) {
            throw new NotFoundException("No users found with username: " + username);
        }

        return userPage.getRecords();
    }

    /**
     * 根据用户名模糊查询用户总数，仅管理员可操作
     *
     * @param username 用户名
     * @return 查询到的用户总数
     */
    @Override
    public int getCountOfUsersByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        return userMapper.selectCount(queryWrapper);
    }

    /**
     * 获取普通用户总数，仅管理员可操作
     *
     * @return 普通用户总数
     */
    @Override
    public int getCountOfNormUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "NORM");
        return userMapper.selectCount(queryWrapper);
    }

    /**
     * 根据用户名获取普通用户总数，仅管理员可操作
     *
     * @param username 用户名
     * @return 指定用户名的普通用户总数
     */
    @Override
    public int getCountOfNormUsersByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", "NORM").like("username", username);
        return userMapper.selectCount(queryWrapper);
    }
}
