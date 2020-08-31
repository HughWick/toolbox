package com.github.hugh;

import com.github.hugh.model.Student;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AS
 * @date 2020/8/31 9:18
 */
public class MapTest {

    @Test
    public void test01() {
        Map map = new HashMap();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "sd");
        map.put("amouont", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            main.java.com.github.hugh.util.MapUtils.toEntity(new Student(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--1->>" + main.java.com.github.hugh.util.MapUtils.isEmpty(map));
        map.clear();
        System.out.println("--2->>" + main.java.com.github.hugh.util.MapUtils.isEmpty(map));
        System.out.println("-0--isSuccess-=>" + main.java.com.github.hugh.util.MapUtils.isSuccess(map, "code", null));
        System.out.println("-1--isSuccess-=>" + main.java.com.github.hugh.util.MapUtils.isSuccess(null, "code", null));
        map.put("code", "0000");
        System.out.println("2-isSuccess---=>" + main.java.com.github.hugh.util.MapUtils.isSuccess(map, "code", "0000"));
        System.out.println("-isFailure---=>" + main.java.com.github.hugh.util.MapUtils.isFailure(map, "code", "00100"));
    }
}


