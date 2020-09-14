package com.github.hugh;

import com.github.hugh.util.ListUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author AS
 * @date 2020/9/14 9:36
 */
public class ListTest {

    @Test
    public void test01() {
        List<Integer> originList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println("--->>" + ListUtils.guavaPartitionList(originList, 4, 3));
    }

    @Test
    public void test02() {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        String str = "{\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\"}";
        String str2 = "1,2,3,4,5,6,7";
        System.out.println("-1-->>"+ ListUtils.guavaArrToList(array));
        System.out.println("--2->>"+ ListUtils.guavaStringToList(str2).size());
    }
}
