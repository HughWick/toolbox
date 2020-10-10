package com.github.hugh.common;

import com.github.hugh.util.common.NamesUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/10/10 17:52
 */
public class NamesTest {

    @Test
    public void test01() {
        System.out.println("--1->>" + NamesUtils.encrypt("刘三"));
        System.out.println("--2->>" + NamesUtils.encrypt("王月如"));
        System.out.println("--3->>" + NamesUtils.encrypt("欧阳震华"));
        System.out.println("--4->>" + NamesUtils.encrypt("麦麦提·赛帕克"));
    }
}
