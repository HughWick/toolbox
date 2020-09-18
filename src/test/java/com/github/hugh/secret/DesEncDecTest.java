package com.github.hugh.secret;

import com.github.hugh.util.secrect.DesEncDecUtils;

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
}
