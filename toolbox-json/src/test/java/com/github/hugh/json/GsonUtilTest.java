package com.github.hugh.json;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.model.Student;
import com.github.hugh.util.DateUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * gson 工具类测试
 *
 * @author AS
 * Date: 2023/1/9 10:10
 */
class GsonUtilTest {

    @Test
    void testFormJson() {
        String create = "1625024713000";
        Student student1 = new Student();
        student1.setId(1);
        student1.setAge(2);
        student1.setName("张三");
        student1.setAmount(10.14);
        student1.setBirthday(null);
//        student1.setSystem(0);
        student1.setCreate(DateUtils.parseTimestamp(create));
        String strJson1 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"birthday\":null,\"create\":\"" + create + "\"}";
        Student student = GsonUtils.fromJson(strJson1, Student.class);
        assertEquals(student1.toString(), student.toString());
        String jsonStr = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":\"2021-06-30 11:45:13\",\"system\":0}";
        assertEquals(jsonStr, JsonObjectUtils.toJson(student));
        String jsonStr2 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":" + create + ",\"system\":0}";
        assertEquals(jsonStr2, JsonObjectUtils.toJsonTimestamp(student));
    }
}
