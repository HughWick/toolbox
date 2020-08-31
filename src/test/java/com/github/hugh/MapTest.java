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
        try {
            main.java.com.github.hugh.util.MapUtils.toEntity(new Student(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--1->>" + main.java.com.github.hugh.util.MapUtils.isEmpty(map));
        map.clear();
        System.out.println("--2->>" + main.java.com.github.hugh.util.MapUtils.isEmpty(map));
    }
}


