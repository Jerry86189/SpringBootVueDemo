package com.jerry86189.springbootvuedemo.config;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/30 17:39
 * @version 1.0
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/30 17:39
 */
@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    private String uploadDir;
}
