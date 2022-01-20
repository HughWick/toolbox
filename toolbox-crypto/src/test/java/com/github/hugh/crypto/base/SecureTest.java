package com.github.hugh.crypto.base;

import com.github.hugh.crypto.AppkeyUtils;
import com.github.hugh.crypto.model.SecureDTO;
import com.github.hugh.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class SecureTest {

    @Test
    void testSecure() {
//        KeyPair rsa = KeyUtil.generateKeyPair("RSA");
//        System.out.println(new String(rsa.getPrivate().getEncoded()));
//        System.out.println(rsa);
//
        System.out.println(AppkeyUtils.generate());
        System.out.println(AppkeyUtils.generateSecret());

        String token = "Th_" + AppkeyUtils.generateSecret() + "_" + AppkeyUtils.generateSecret();
        System.out.println("--->>" + token);
    }


    @Test
    void test01() {
        String appkey = "34c585bce6ea4a289f67fe0e55ebcbeb";
        String appkeySecret = "WkiydfkHBuzfvzEV4Hr0FfhpysrNvIDOlTdenPyF8QpnQxDGTljULXFS8v69IcWs";
        String token = "Th_5nVZp19WHXcDXJeQ3FKIQ8SZG4EMw8S3erARzYMywoJ0Q0q4L7BU1KsrzyzE0Yp3_1zsZ7pnyaVwX4T77KJEmOE5WKtPDd7XmnVR9QCB7nvYPkDdsU18HhGH5fzJQyLTi";
        SecureDTO secureDTO = new SecureDTO();
        secureDTO.setAppkey(appkey);
        secureDTO.setAppSecret(appkeySecret);
        secureDTO.setToken(token);
        Date date = DateUtils.parse("2022-01-20 10:35:00");
        long l = date.getTime();
        String timestamp = String.valueOf(System.currentTimeMillis());
//        String timestamp = String.valueOf(date.getTime());
        secureDTO.setTimestamp(timestamp);
        String sign = SecureUtils.signature(appkey, token, secureDTO.getData(), secureDTO.getNotifyUrl(), timestamp);
        secureDTO.setSign(sign);
        System.out.println("---->>>" + sign);

        System.out.println("---->>" + SecureUtils.verifySignature(secureDTO));
    }

    @Test
    void test02() {
        Date date = DateUtils.parse("2022-01-20 10:35:00");
        long l = date.getTime();
        System.out.println("===0==>>>" + l);
        boolean b = SecureUtils.verifyTimestamp(l, 20);
        System.out.println("===>>" + b);
    }
}
