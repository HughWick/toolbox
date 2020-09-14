package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.MapUtils;
import net.sf.json.JSONObject;
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
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        Student student = new Student();
        try {
           MapUtils.toEntity(student, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSONObject.fromObject(student));
        System.out.println("--1->>" + MapUtils.isEmpty(map));
        map.clear();
        System.out.println("--2->>" + MapUtils.isEmpty(map));
        System.out.println("--2->>" + MapUtils.isNotEmpty(map));
        System.out.println("-0--isSuccess-=>" + MapUtils.isSuccess(map, "code", null));
        System.out.println("-1--isSuccess-=>" + MapUtils.isSuccess(null, "code", null));
        map.put("code", "0000");
        System.out.println("2-isSuccess---=>" + MapUtils.isSuccess(map, "code", "0000"));
        System.out.println("-isFailure---=>" + MapUtils.isFailure(map, "code", "00100"));
    }

    @Test
    public void test02() {
        Map map = new HashMap();
        System.out.println("--1->"+ MapUtils.getMap(map, "data"));
        System.out.println("-2-->"+ MapUtils.getString(map, "data"));
        System.out.println("-3-->"+ MapUtils.getInt(map, "data"));
        System.out.println("-4-->"+ MapUtils.getLong(map, "data"));
        System.out.println("-5-->"+ MapUtils.getDouble(map, "data"));
    }

}


