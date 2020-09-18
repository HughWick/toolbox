package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.SerializeUtils;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AS
 * @date 2020/8/31 17:59
 */
public class SerializeTest {

    @Test
    public void test01() {
        String str = "asdasdfdsafaeriuowqeyr4iu13y3iu4gh21juk3739yrfiohdkjcsjdnf`2随身带哦发i是大家都会覅凯撒觉得哈佛i安身的地方哈吉斯的话覅绿卡和肉IP就和外婆而减弱为啊圣诞快乐付款就会考虑就撒娇就发c";
        System.out.println("--->" + SerializeUtils.toBytes(str));
        System.out.println("--->" + SerializeUtils.toObject(SerializeUtils.toBytes(str)));
    }

    @Test
    public void test02() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Object o = MapUtils.toEntity(Student.class, map);
            byte[] bytes = SerializeUtils.toBytes(o);
            System.out.println("-1-->" + bytes);
            Student student = (Student) SerializeUtils.toObject(bytes);
            System.out.println("--->" + student);
            System.out.println("--->" + JSONObject.fromObject(student));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
