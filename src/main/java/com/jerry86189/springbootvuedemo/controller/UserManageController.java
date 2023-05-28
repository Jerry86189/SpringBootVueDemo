package com.jerry86189.springbootvuedemo.controller;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/17 9:18
 * @version 1.0
 */

import com.jerry86189.springbootvuedemo.dto.*;
import com.jerry86189.springbootvuedemo.entity.User;
import com.jerry86189.springbootvuedemo.exceptions.*;
import com.jerry86189.springbootvuedemo.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @date 2023/5/17 9:18
 * @description 用户管理控制器类。本类主要用于处理用户相关的请求，如登录、注册等操作
 */
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
@ControllerAdvice
public class UserManageController {
    @Autowired
    private UserService userService;

    /**
     * 提供用户访问登录页面的接口
     *
     * @return 返回登录页面
     */
//    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    /**
     * 处理用户登录请求的接口
     *
     * @param loginRequest 用户的登录信息
     * @return 返回登录结果，如果登录成功，返回用户相关信息和token，如果失败，抛出异常
     */
//    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @NotNull LoginRequest loginRequest) {
        System.out.println("enter login: (post: /users/login)");
        LoginResponse login = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // Create an Authentication object
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                AuthorityUtils.createAuthorityList(login.getUser().getRole().getValue())
        );
        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.ok().body(login);
    }

    /**
     * 用于处理登录失败的异常
     *
     * @param e 登录失败抛出的异常
     * @return 返回错误信息
     */
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailedException(@NotNull LoginFailedException e) {
        System.out.println("enter handleLoginFailedException");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * 处理用户注册请求的接口
     *
     * @param registerRequest 用户的注册信息
     * @return 如果注册成功，返回成功状态码，如果失败，抛出异常
     */
//    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @NotNull RegisterRequest registerRequest) {
        System.out.println("enter register: (post: /users/register)");
        User user = registerRequest.getUser();

        if (user.getRole().getValue().equals("ADMIN")) {
            try {
                userService.login(registerRequest.getOtherAdminUsername(), registerRequest.getOtherAdminPassword());
            } catch (LoginFailedException e) {
                throw new LoginFailedException("Invalid other admin credentials.");
            }
        }

        userService.register(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 用于处理注册失败的异常
     *
     * @param e 注册失败抛出的异常
     * @return 返回错误信息
     */
    @ExceptionHandler(RegisterFailedException.class)
    public ResponseEntity<String> handleRegisterFailedException(@NotNull RegisterFailedException e) {
        System.out.println("enter handleRegisterFailedException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * 删除自身用户账户，仅用户本人可操作
     *
     * @param deleteSelfRequest 删除请求信息，包含用户名和密码
     * @return HTTP状态码，成功返回200
     */
    @DeleteMapping("/delete_self_account")
    public ResponseEntity<Void> deleteSelfAccount(@RequestBody @NotNull DeleteSelfRequest deleteSelfRequest) {
        System.out.println("enter deleteSelfAccount");
        userService.deleteAccount(deleteSelfRequest.getUsername(), deleteSelfRequest.getPassword());
        return ResponseEntity.ok().build();
    }

    /**
     * 对DeleteFailedException的处理方法
     *
     * @param e DeleteFailedException实例
     * @return HTTP状态码和错误信息，状态码为400
     */
    @ExceptionHandler(DeleteFailedException.class)
    public ResponseEntity<String> handleDeleteFailedException(@NotNull DeleteFailedException e) {
        System.out.println("enter handleDeleteFailedException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * 更新自身用户账户信息，仅用户本人可操作
     *
     * @param updateSelfRequest 更新请求信息，包含用户id，密码和用户新信息
     * @return HTTP状态码，成功返回200
     */
    @PutMapping("/update_self_account")
    public ResponseEntity<User> updateSelfAccount(@RequestBody @NotNull UpdateSelfRequest updateSelfRequest) {
        System.out.println("(updateSelfRequest.getVerifyId(): " + updateSelfRequest.getVerifyId());
        System.out.println("updateSelfRequest.getVerifyPassword(): "+updateSelfRequest.getVerifyPassword());
        System.out.println("updateSelfRequest.getUser(): "+updateSelfRequest.getUser().toString());
        User updateUserInfo = userService.updateUserInfo(updateSelfRequest.getVerifyId(), updateSelfRequest.getVerifyPassword(), updateSelfRequest.getUser());
        return ResponseEntity.ok(updateUserInfo);
    }

    /**
     * 对UpdateFailedException的处理方法
     *
     * @param e UpdateFailedException实例
     * @return HTTP状态码和错误信息，状态码为400
     */
    @ExceptionHandler(UpdateFailedException.class)
    public ResponseEntity<String> handleUpdateFailedException(@NotNull UpdateFailedException e) {
        System.out.println("enter handleUpdateFailedException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * 分页获取普通用户信息
     *
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 用户信息列表和分页信息
     */
    @GetMapping("/get_users")
    private ResponseEntity<GetUsersResponse> getUsersByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        List<User> users = userService.getAllUsersByPage(pageNum, pageSize);
        int totalCount = userService.getCountOfUsers();

        GetUsersResponse usersResponse = new GetUsersResponse();
        usersResponse.setUsers(users);
        usersResponse.setCurrentPage(pageNum);
        usersResponse.setPageSize(pageSize);
        usersResponse.setTotalCount(totalCount);

        int totalPages = totalCount / pageSize;
        if (totalCount % pageSize == 0) {
            totalPages++;
        }
        usersResponse.setTotalPages(totalPages);
        return ResponseEntity.ok(usersResponse);
    }

    /**
     * 对PageQueryFailedException的处理方法
     *
     * @param e PageQueryFailedException实例
     * @return HTTP状态码和错误信息，状态码为500
     */
    @ExceptionHandler(PageQueryFailedException.class)
    public ResponseEntity<String> handlePageQueryFailedException(@NotNull PageQueryFailedException e) {
        System.out.println("enter handlePageQueryFailedException");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 查询到的用户信息
     */
    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 对UserNotFoundException的处理方法
     *
     * @param e UserNotFoundException实例
     * @return HTTP状态码和错误信息，状态码为404
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(@NotNull UserNotFoundException e) {
        System.out.println("enter handleUserNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * 根据用户名获取用户信息，支持分页
     *
     * @param username 用户名
     * @param pageNum  当前页码
     * @param pageSize 每页展示的用户数量
     * @return 用户信息列表和分页信息
     */
    @GetMapping("/get_by_name/{username}")
    public ResponseEntity<GetUsersResponse> getUsersByUsername(@PathVariable String username, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        List<User> users = userService.getUserByUsername(username, pageNum, pageSize);
        int totalCount = userService.getCountOfUsersByUsername(username);

        GetUsersResponse getUsersResponse = new GetUsersResponse();
        getUsersResponse.setUsers(users);
        getUsersResponse.setCurrentPage(pageNum);
        getUsersResponse.setTotalCount(totalCount);

        int totalPages = totalCount / pageSize;
        if (totalCount % pageSize == 0) {
            totalPages++;
        }
        getUsersResponse.setTotalPages(totalPages);
        return ResponseEntity.ok().body(getUsersResponse);
    }

    /**
     * 根据用户id删除普通用户账户
     *
     * @param id 用户id
     * @return HTTP状态码，成功返回200
     */
    @DeleteMapping("/delete_norm_by_id/{id}")
    public ResponseEntity<Void> deleteNormUserById(@PathVariable Long id) {
        userService.deleteNormUserById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取所有普通用户信息（分页）
     *
     * @param pageNum  请求的页数
     * @param pageSize 每页的用户数量
     * @return 包含普通用户信息和分页数据的响应
     */
    @GetMapping("/get_norm_users")
    public ResponseEntity<GetUsersResponse> getNormUsersByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        List<User> users = userService.getAllNormUsersByPage(pageNum, pageSize);
        int totalCount = userService.getCountOfNormUsers();

        GetUsersResponse getUsersResponse = new GetUsersResponse();
        getUsersResponse.setUsers(users);
        getUsersResponse.setCurrentPage(pageNum);
        getUsersResponse.setPageSize(pageSize);
        getUsersResponse.setTotalCount(totalCount);

        int totalPages = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
        getUsersResponse.setTotalPages(totalPages);

        return ResponseEntity.ok().body(getUsersResponse);
    }

    /**
     * 根据用户ID获取普通用户的信息
     *
     * @param id 需要查询的普通用户ID
     * @return 包含普通用户信息的响应
     */
    @GetMapping("/get_norm_by_id/{id}")
    public ResponseEntity<User> getNormUserById(@PathVariable Long id) {
        User user = userService.getNormUserById(id);
        return ResponseEntity.ok().body(user);
    }

    /**
     * 根据用户名获取普通用户信息（分页）
     *
     * @param username 需要查询的普通用户用户名
     * @param pageNum  请求的页数
     * @param pageSize 每页的用户数量
     * @return 包含普通用户信息和分页数据的响应
     */
    @GetMapping("/get_norm_by_name/{username}")
    public ResponseEntity<GetUsersResponse> getNormUsersByUsername(@PathVariable String username, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        List<User> users = userService.getNormUserByUsername(username, pageNum, pageSize);
        int totalCount = userService.getCountOfNormUsersByUsername(username);

        GetUsersResponse getUsersResponse = new GetUsersResponse();
        getUsersResponse.setUsers(users);
        getUsersResponse.setCurrentPage(pageNum);
        getUsersResponse.setPageSize(pageSize);
        getUsersResponse.setTotalCount(totalCount);

        int totalPages = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
        getUsersResponse.setTotalPages(totalPages);

        return ResponseEntity.ok().body(getUsersResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok().body("Test Get successful");
    }

    @PostMapping("/test")
    public ResponseEntity<String> testPost() {
        return ResponseEntity.ok().body("Test Post successful");
    }
}
