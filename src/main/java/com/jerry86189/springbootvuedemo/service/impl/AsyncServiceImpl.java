package com.jerry86189.springbootvuedemo.service.impl;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/6/2 15:26
 * @version 1.0
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/6/2 15:26
 */
@Service("AsyncService")
public class AsyncServiceImpl {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    @Autowired
    public AsyncServiceImpl(OkHttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }
}
