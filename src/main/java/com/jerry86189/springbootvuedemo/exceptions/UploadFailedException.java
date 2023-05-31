package com.jerry86189.springbootvuedemo.exceptions;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/30 17:59
 * @version 1.0
 */

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/30 17:59
 */
public class UploadFailedException extends RuntimeException {
    public UploadFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
