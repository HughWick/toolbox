package com.github.hugh.support.gson;

import com.github.hugh.util.gson.JsonArrayUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

;

/**
 * User: AS
 * Date: 2021/10/2 18:50
 */
public class JsonArrayTest {

    @Test
    void testArray(){
        JsonArray jsonArray = new JsonArray();
        assertTrue(JsonArrayUtils.isEmpty(jsonArray));
        JsonElement jsonElement =null;
        jsonArray.add(jsonElement);
        assertTrue(JsonArrayUtils.isNotEmpty(jsonArray));
//        assertFalse(JsonArrayUtils.isNotEmpty(jsonArray));
    }
}
