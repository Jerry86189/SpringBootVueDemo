package com.jerry86189.springbootvuedemo.exceptions;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/16 20:10
 * @version 1.0
 */

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/16 20:10
 */
public class UpdateFailedException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UpdateFailedException(String message) {
        super(message);
    }
}
