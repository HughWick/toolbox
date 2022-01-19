package com.github.hugh.crypto.base;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;

public class SecureTest {

    @Test
    void testSecure() {
        KeyPair rsa = KeyUtil.generateKeyPair("RSA");
        System.out.println(new String(rsa.getPrivate().getEncoded()));
        System.out.println(rsa);
    }
}
