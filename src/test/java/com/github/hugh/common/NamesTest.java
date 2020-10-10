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
        System.out.println("--1->>" + NamesUtils.hide("刘三"));
        System.out.println("--2->>" + NamesUtils.hide("王月如"));
        System.out.println("--3->>" + NamesUtils.hide("欧阳震华"));
        System.out.println("--4->>" + NamesUtils.hide("麦麦提·赛帕克"));
    }
}
