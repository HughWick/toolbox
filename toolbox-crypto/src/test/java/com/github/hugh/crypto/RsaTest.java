package com.github.hugh.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class RsaTest {

    @Test
    void testRsa() throws Exception {
        RsaUtils instance = RsaUtils.getInstance();
        instance.genKeyPair();
        String data = "sajhdasjkdhfkjqwehijrhioqwehrio";
        String s = new String(instance.encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8)));
        System.out.println("->>>"+s);
    }
}
