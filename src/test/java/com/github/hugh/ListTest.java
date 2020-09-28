package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.ListUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
        String strings = "[\"Saab\", \"Volvo\", \"BMW\"]";
        String str = "{\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\"}";
        String str2 = "1,2,3,4,5,6,7";
//        System.out.println("-1-->>" + ListUtils.guavaArrToList(array));
        System.out.println("-2-->>" + ListUtils.guavaStringToList(strings));
        System.out.println("--3->>" + ListUtils.guavaStringToList(str2).size());
    }

//    @Test
//    public void test03() {
//        // 姓名，性别，年龄，薪资，级别，籍贯
//        List<List<Object>> lists = Lists.newArrayList();
//        lists.add(Arrays.asList("张三", "男", 22, 10000, "T2", "贵州遵义"));
//        lists.add(Arrays.asList("李四", "女", 23, 15000, "T2", "贵州遵义"));
//        lists.add(Arrays.asList("王五", "女", 23, 12000, "T3", "北京海淀"));
//        lists.add(Arrays.asList("王六", "男", 23, 13000, "T3", "北京昌平"));
//        lists.add(Arrays.asList("王七", "男", 24, 14000, "T3", "北京昌平"));
//        lists.add(Arrays.asList("王八", "女", 23, 12000, "T2", "北京昌平"));
//        lists.add(Arrays.asList("胡三", "男", 26, 12000, "T3", "北京朝阳"));
//        lists.add(Arrays.asList("胡四", "男", 26, 13000, "T3", "北京朝阳"));
//        lists.add(Arrays.asList("张五", "女", 26, 14000, "T3", "北京海淀"));
//        lists.add(Arrays.asList("张六", "男", 27, 17000, "T4", "北京朝阳"));
//        lists.add(Arrays.asList("张七", "男", 23, 12000, "T3", "北京朝阳"));
//        lists.add(Arrays.asList("黄五", "女", 24, 11000, "T2", "北京海淀"));
//        lists.add(Arrays.asList("黄三", "男", 24, 10000, "T2", "北京大兴"));
//        lists.add(Arrays.asList("刘爱", "男", 22, 9000, "T1", "北京大兴"));
//        lists.add(Arrays.asList("刘跟", "男", 39, 18000, "T4", "贵州遵义"));
//        lists.add(Arrays.asList("李根", "男", 28, 20000, "T5", "贵州贵阳"));
//        lists.add(Arrays.asList("郭艾琳", "男", 24, 12000, "T3", "贵州贵阳"));
//        Map<Integer, Boolean> colSortMaps = new HashMap<>();
//        colSortMaps.put(2, true); // 年龄升序
//        colSortMaps.put(3, true); // 薪资升序
//        colSortMaps.put(4, true); // 级别升序
//        lists.sort(ListUtils.listComparator(colSortMaps));
//        System.out.println("-2-->>" + lists);
//    }


    @Test
    public void test04() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "Mahesh", 12));
        list.add(new Student(2, "Suresh", 15));
        list.add(new Student(3, "Nilesh", 10));
        System.out.println("---Sorting using Comparator by Age---");
        List<Student> collect = list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        collect.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));

        System.out.println("---Sorting using Comparator by Age in reverse order---");
        list.stream().sorted(Comparator.comparing(Student::getAge).reversed())
                .forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));
    }
}
