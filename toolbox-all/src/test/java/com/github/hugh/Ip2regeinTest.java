package com.github.hugh;

import com.github.hugh.model.dto.Ip2regionDTO;
import com.github.hugh.util.ip.Ip2regionUtils;

/**
 * @author AS
 * @date 2021/2/23 14:23
 */
public class Ip2regeinTest {

    public static void main(String[] args) throws Exception {
//        System.out.println(Ip2regionUtils.getCityInfo("220.248.12.158"));
        System.out.println(Ip2regionUtils.get("222.244.144.131"));
        Ip2regionDTO parse = Ip2regionUtils.parse(null);
        System.out.println(parse.getCountry());
//        Ip2regionUtils ip2regionUtils = new Ip2regionUtils();
//        ip2regionUtils.test01("/ip2region/ip2region.db");
        System.out.println("--->>");
    }
}
