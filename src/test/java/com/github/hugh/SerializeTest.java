package com.github.hugh;

import com.github.hugh.util.SerializeUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/8/31 17:59
 */
public class SerializeTest {

    @Test
    public void test01() {
        String str = "asdc";
        System.out.println("--->" + SerializeUtils.toBytes(str));
        System.out.println("--->" + SerializeUtils.toObject(SerializeUtils.toBytes(str)));
    }
}
