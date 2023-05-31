package com.jerry86189.springbootvuedemo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jerry86189.springbootvuedemo.dto.LoginResponse;
import com.jerry86189.springbootvuedemo.entity.User;
import com.jerry86189.springbootvuedemo.enumpack.Role;
import com.jerry86189.springbootvuedemo.exceptions.*;
import com.jerry86189.springbootvuedemo.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @description TODO
 * @date 2023/5/18 0:06
 */
@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loginWithCorrectCredentials() {
        String username = "testUsername";
        String password = "testPassword";

        // Encrypt the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);

        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword(encryptedPassword);

        // Set the role here.
        testUser.setRole(Role.ADMIN);  // or Role.NORM

        when(userMapper.selectOne(any())).thenReturn(testUser);

        LoginResponse result = userService.login(username, password);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(result.getUser().getRole(), testUser.getRole());
    }

    @Test
    public void loginWithIncorrectCredentials() {
        String username = "testUsername";
        String password = "wrongPassword";

        when(userMapper.selectOne(any())).thenReturn(null);

        assertThrows(LoginFailedException.class, () -> {
            userService.login(username, password);
        });
    }

    @Test
    public void successfulRegistration() {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        when(userMapper.insert(any())).thenReturn(1);

        User registeredUser = userService.register(testUser);

        assertNotNull(registeredUser);
        assertEquals(registeredUser.getUsername(), testUser.getUsername());
        assertEquals(registeredUser.getPassword(), testUser.getPassword());
    }

    @Test
    public void failedRegistration() {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        when(userMapper.insert(any())).thenReturn(0);

        assertThrows(RegisterFailedException.class, () -> {
            userService.register(testUser);
        });
    }

    @Test
    public void deleteAccountSuccess() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        when(userMapper.selectOne(any())).thenReturn(user);

        assertDoesNotThrow(() -> userService.deleteAccount(username, password));
    }

    @Test
    public void deleteAccountFailed() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode("wrongPassword"));

        when(userMapper.selectOne(any())).thenReturn(user);

        assertThrows(DeleteFailedException.class, () -> userService.deleteAccount(username, password));
    }

    @Test
    public void updateUserInfoSuccess() {
        Long userId = 1L;
        String oldPassword = "testPassword";
        String newPassword = "newPassword";
        User testUser = new User();
        testUser.setUserId(userId);
        testUser.setPassword(newPassword);

        User originalUser = new User();
        originalUser.setUserId(userId);
        originalUser.setPassword(new BCryptPasswordEncoder().encode(oldPassword));

        when(userMapper.selectById(userId)).thenReturn(originalUser);
        when(userMapper.updateById(testUser)).thenReturn(1);

        User updatedUser = userService.updateUserInfo(userId, oldPassword, testUser);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getUserId(), testUser.getUserId());
        assertEquals(new BCryptPasswordEncoder().matches(newPassword, updatedUser.getPassword()), true);
    }

    @Test
    public void updateUserInfoFailed() {
        Long userId = 1L;
        String oldPassword = "wrongPassword";
        User testUser = new User();
        testUser.setUserId(userId);
        testUser.setPassword("newPassword");

        User originalUser = new User();
        originalUser.setUserId(userId);
        originalUser.setPassword(new BCryptPasswordEncoder().encode("testPassword"));

        when(userMapper.selectById(userId)).thenReturn(originalUser);

        assertThrows(UpdateFailedException.class, () -> userService.updateUserInfo(userId, oldPassword, testUser));
    }

    @Test
    public void getAllUsersByPage() {
        int pageNum = 1;
        int pageSize = 5;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            User user = new User();
            user.setUserId((long) i);
            user.setUsername("User" + i);
            users.add(user);
        }

        IPage<User> userPage = new Page<>();
        userPage.setRecords(users);

        when(userMapper.selectPage(any(), any())).thenReturn(userPage);

        List<User> result = userService.getAllUsersByPage(pageNum, pageSize);

        assertNotNull(result);
        assertEquals(result.size(), pageSize);
    }

    @Test
    public void getUserById() {
        Long id = 1L;

        User user = new User();
        user.setUserId(id);
        user.setUsername("User1");

        when(userMapper.selectById(any())).thenReturn(user);

        User result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(result.getUserId(), id);
    }

    @Test
    public void getUserByUsernameSuccess() {
        String username = "testUser";
        int pageNum = 1;
        int pageSize = 5;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            User user = new User();
            user.setUserId((long) i);
            user.setUsername(username);
            users.add(user);
        }

        IPage<User> userPage = new Page<>();
        userPage.setRecords(users);

        when(userMapper.selectPage(any(), any())).thenReturn(userPage);

        List<User> result = userService.getUserByUsername(username, pageNum, pageSize);

        assertNotNull(result);
        assertEquals(result.size(), pageSize);
        assertTrue(result.stream().allMatch(u -> username.equals(u.getUsername())));
    }

    @Test
    public void deleteNormUserByIdSuccess() {
        Long id = 1L;

        when(userMapper.deleteById(any())).thenReturn(1);

        assertDoesNotThrow(() -> userService.deleteNormUserById(id));
    }

    @Test
    public void deleteNormUserByIdFailed() {
        Long id = 1L;

        when(userMapper.deleteById(any())).thenReturn(0);

        assertThrows(DeleteFailedException.class, () -> {
            userService.deleteNormUserById(id);
        });
    }

    @Test
    public void getCountOfUsers() {
        int count = 5;

        when(userMapper.selectCount(any())).thenReturn(count);

        int result = userService.getCountOfUsers();

        assertEquals(count, result);
    }

    @Test
    public void getAllNormUsersByPageSuccess() {
        int pageNum = 1;
        int pageSize = 5;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            User user = new User();
            user.setUserId((long) i);
            user.setUsername("testUser");
            user.setRole(Role.NORM);
            users.add(user);
        }

        IPage<User> userPage = new Page<>();
        userPage.setRecords(users);

        when(userMapper.selectPage(any(), any())).thenReturn(userPage);

        List<User> result = userService.getAllNormUsersByPage(pageNum, pageSize);

        assertNotNull(result);
        assertEquals(result.size(), pageSize);
        assertTrue(result.stream().allMatch(u -> Role.NORM.equals(u.getRole())));
    }

    @Test
    public void getNormUserByIdSuccess() {
        Long id = 1L;
        User user = new User();
        user.setUserId(id);
        user.setUsername("testUser");
        user.setRole(Role.NORM);

        when(userMapper.selectOne(any())).thenReturn(user);

        User result = userService.getNormUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getUserId());
        assertEquals(Role.NORM, result.getRole());
    }

    @Test
    public void getNormUserByIdNotFound() {
        Long id = 1L;

        when(userMapper.selectOne(any())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            userService.getNormUserById(id);
        });
    }

    @Test
    public void testGetCountOfUsersByUsername_UserExists() {
        // assume there are 2 users with username containing "jerry"
        when(userMapper.selectCount(any())).thenReturn(2);

        String username = "jerry";
        int userCount = userService.getCountOfUsersByUsername(username);

        assertEquals(2, userCount, "Expected 2 users with username containing 'jerry'");
    }

    @Test
    public void testGetCountOfUsersByUsername_UserDoesNotExist() {
        // assume there are no users with username containing "nonexistent"
        when(userMapper.selectCount(any())).thenReturn(0);

        String username = "nonexistent";
        int userCount = userService.getCountOfUsersByUsername(username);

        assertEquals(0, userCount, "Expected 0 users with username containing 'nonexistent'");
    }

    @Test
    public void getCountOfNormUsers() {
        int count = 5;

        when(userMapper.selectCount(any())).thenReturn(count);

        int result = userService.getCountOfNormUsers();

        assertEquals(count, result);
    }

    @Test
    public void getNormUserByUsernameSuccess() {
        String username = "testUser";
        int pageNum = 1;
        int pageSize = 5;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            User user = new User();
            user.setUserId((long) i);
            user.setUsername(username);
            user.setRole(Role.NORM);
            users.add(user);
        }

        IPage<User> userPage = new Page<>();
        userPage.setRecords(users);

        when(userMapper.selectPage(any(), any())).thenReturn(userPage);

        List<User> result = userService.getNormUserByUsername(username, pageNum, pageSize);

        assertNotNull(result);
        assertEquals(result.size(), pageSize);
        assertTrue(result.stream().allMatch(u -> Role.NORM.equals(u.getRole())));
        assertTrue(result.stream().allMatch(u -> username.equals(u.getUsername())));
    }

    @Test
    public void getNormUserByUsernameNotFound() {
        String username = "testUser";
        int pageNum = 1;
        int pageSize = 5;

        IPage<User> userPage = new Page<>();
        userPage.setRecords(Collections.emptyList());

        when(userMapper.selectPage(any(), any())).thenReturn(userPage);

        assertThrows(NotFoundException.class, () -> {
            userService.getNormUserByUsername(username, pageNum, pageSize);
        });
    }

    @Test
    public void getCountOfNormUsersByUsername() {
        String username = "testUser";
        int count = 5;

        when(userMapper.selectCount(any())).thenReturn(count);

        int result = userService.getCountOfNormUsersByUsername(username);

        assertEquals(count, result);
    }

    @Test
    public void getCountOfNormUsersSuccess() {
        int count = 5;
        when(userMapper.selectCount(any())).thenReturn(count);
        int result = userService.getCountOfNormUsers();
        assertEquals(count, result);
    }

    @Test
    public void getCountOfNormUsersByUsernameSuccess() {
        String username = "testUser";
        int count = 3;

        when(userMapper.selectCount(any())).thenReturn(count);

        int result = userService.getCountOfNormUsersByUsername(username);

        assertEquals(count, result);
    }
}