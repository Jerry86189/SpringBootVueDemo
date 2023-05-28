package com.jerry86189.springbootvuedemo.exceptions;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/16 19:54
 * @version 1.0
 */

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/16 19:54
 */
public class DeleteFailedException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DeleteFailedException(String message) {
        super(message);
    }
}
