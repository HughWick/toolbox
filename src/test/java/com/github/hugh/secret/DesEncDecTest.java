package com.github.hugh.secret;

import com.github.hugh.util.secrect.DesEncDecUtils;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author AS
 * @date 2020/9/18 15:10
 */
public class DesEncDecTest {

    public static void main(String[] args) throws Exception {
        DesEncDecUtils Des = DesEncDecUtils.getInstance("yinmeng0000w");
        System.out.println(Des.encrypt("13825004872"));
        System.out.println(Des.encrypt("md5pass"));
        System.out.println("===>>" + Des.decrypt("4LJigdM+uWM="));
//        System.out.println(Des.decrypt(Des.encrypt("46010319821218091X")));
    }


    @Test
    public void test01() {
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance("cmmop_app");
        try {
            String t1 = desEnc.encrypt("0ec4dbfdfb7945f0a6ca61fd14065a77");
            System.out.println(t1);
            String encodeURL1 = URLEncoder.encode(t1, "UTF-8");
            System.out.println("-encodeURL-->>" + encodeURL1);
            String decode = URLDecoder.decode(encodeURL1, "UTF-8");
            System.out.println("-decode--->>" + decode);
            System.out.println(desEnc.decrypt(decode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
