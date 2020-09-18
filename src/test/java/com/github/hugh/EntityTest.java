package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AS
 * @date 2020/9/18 10:01
 */
public class EntityTest {

    @Test
    public void test01() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Object o = MapUtils.toEntity(Student.class, map);
            Student o1 = (Student) EntityUtils.deepClone(o);
            System.out.println(o + "====" + o1);
            System.out.println(JSONObject.fromObject(o));
            System.out.println(JSONObject.fromObject(o1));
            o1.setName("张三");
            System.out.println(JSONObject.fromObject(o1));
            System.out.println(JSONObject.fromObject(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}