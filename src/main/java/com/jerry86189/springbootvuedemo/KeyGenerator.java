package com.jerry86189.springbootvuedemo;/**
 * @project SpringBootVueDemo
 * @description TODO
 * @author Jerry
 * @date 2023/5/18 0:22
 * @version 1.0
 */

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * @author Jerry
 * @version 1.0
 * @description: TODO
 * @date 2023/5/18 0:22
 */
public class KeyGenerator {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(encodedKey);
    }
}