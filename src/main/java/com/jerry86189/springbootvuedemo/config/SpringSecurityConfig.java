package com.jerry86189.springbootvuedemo.config;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/22 23:23
 * @version 1.0
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Jerry
 * @version 1.0
 * @project SpringBootVueDemo
 * @date 2023/5/22 23:23
 * @description Spring Security配置类，用于配置Spring Security的认证和授权规则
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 配置认证管理器，使用自定义的身份验证提供程序
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    /**
     * 配置Spring Security的安全规则，包括哪些请求需要认证，哪些请求需要授权，以及登录和注销的处理方式
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login.html", "/register.html", "/users/login", "/users/register").permitAll()
                .antMatchers("/admin/**",
                        "/users/get_users", "/users/get_by_id/**", "/users/get_by_name/**",
                        "/users/delete_norm_by_id/**", "/users/get_norm_users", "/users/get_norm_by_id/**",
                        "/users/get_norm_by_name/**").hasAuthority("ADMIN")
                .antMatchers("/user/**").hasAuthority("NORM")
                .antMatchers("/users/delete_self_account", "/users/update_self_account").hasAnyAuthority("ADMIN", "NORM")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login.html")
                .and()
                .logout().logoutSuccessUrl("/login.html")
                .and()
                .exceptionHandling().accessDeniedPage("/403.html");
    }

    /**
     * 定义自定义的身份验证提供程序，使用自定义的UserDetailsService和密码编码器
     *
     * @return 返回配置后的身份验证提供程序对象
     */
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }
}
