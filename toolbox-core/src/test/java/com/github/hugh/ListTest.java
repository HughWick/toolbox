package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.ListUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AS
 * @date 2020/9/14 9:36
 */
public class ListTest {

    @Test
    void test01() {
//        var originList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
//        originList.add(9);
        List<String> originList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            originList.add(i+"");
        }
//        System.out.println(originList);
//        System.out.println("--->>" + ListUtils.guavaPartitionList(originList, 4, 3));
        StopWatch stopWatch =new StopWatch();
        stopWatch.start("开始");
        String s = ListUtils.listToString(originList);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        System.out.println("---1->>" + s);
        System.out.println("--2-->>" + ListUtils.guavaStringToList(s));
    }

    @Test
    void test02() {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        String strings = "[\"Saab\", \"Volvo\", \"BMW\" ,\"   \"]";
        String str = "{\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\"}";
        String str2 = "1,2 , 3, 4 ,5,6,";
        String str3 = "[1,2,3,4,5,]";
        String str4 = "[1,2,3,4,5,6, \"7\"]";
        String str5 = "[[a,b,c],1,2,3,4,5,6, \"7\",89]";
        ListUtils.guavaStringToList(strings).forEach(System.out::println);
        System.out.println("-1-->>" + ListUtils.guavaStringToList(strings));
        ListUtils.guavaStringToList(str).forEach(System.out::println);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        ListUtils.guavaStringToList(str2).forEach(System.out::println);
        System.out.println("--------------");
        ListUtils.guavaStringToList(str3).forEach(System.out::println);
        System.out.println("==============");
        ListUtils.guavaStringToList(str4).forEach(System.out::println);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
        ListUtils.guavaStringToList(str5).forEach(System.out::println);
        System.out.println("-````````````````````````");
//        System.out.println("--3->>" + ListUtils.guavaStringToList(str2).size());
    }

    //无实体、针对字段排序排序
    @Test
    public void test03() {
        // 姓名，性别，年龄，薪资，级别，籍贯
        List<List<Object>> lists = Lists.newArrayList();
        lists.add(Arrays.asList("张三", "男", 22, 10000, "T2", "贵州遵义"));
        lists.add(Arrays.asList("李四", "女", 23, 15000, "T2", "贵州遵义"));
        lists.add(Arrays.asList("王五", "女", 23, 12000, "T3", "北京海淀"));
        lists.add(Arrays.asList("王六", "男", 23, 13000, "T3", "北京昌平"));
        lists.add(Arrays.asList("王七", "男", 24, 14000, "T3", "北京昌平"));
        lists.add(Arrays.asList("王八", "女", 23, 12000, "T2", "北京昌平"));
        lists.add(Arrays.asList("胡三", "男", 26, 12000, "T3", "北京朝阳"));
        lists.add(Arrays.asList("胡四", "男", 26, 13000, "T3", "北京朝阳"));
        lists.add(Arrays.asList("张五", "女", 26, 14000, "T3", "北京海淀"));
        lists.add(Arrays.asList("张六", "男", 27, 17000, "T4", "北京朝阳"));
        lists.add(Arrays.asList("张七", "男", 23, 12000, "T3", "北京朝阳"));
        lists.add(Arrays.asList("黄五", "女", 24, 11000, "T2", "北京海淀"));
        lists.add(Arrays.asList("黄三", "男", 24, 10000, "T2", "北京大兴"));
        lists.add(Arrays.asList("刘爱", "男", 22, 9000, "T1", "北京大兴"));
        lists.add(Arrays.asList("刘跟", "男", 39, 18000, "T4", "贵州遵义"));
        lists.add(Arrays.asList("李根", "男", 28, 20000, "T5", "贵州贵阳"));
        lists.add(Arrays.asList("郭艾琳", "男", 24, 12000, "T3", "贵州贵阳"));
        Map<Integer, Boolean> colSortMaps = new HashMap<>();
        colSortMaps.put(2, true); // 年龄升序
        colSortMaps.put(3, true); // 薪资升序
        colSortMaps.put(4, true); // 级别升序
        lists.sort(listComparator(colSortMaps));
        System.out.println("-2-->>" + lists);
    }

    private static Comparator<List<Object>> listComparator(Map<Integer, Boolean> colSortMaps) {
        Ordering ordering = Ordering.natural();
        return (list1, list2) -> {
            ComparisonChain compareChain = ComparisonChain.start();
            for (Integer index : colSortMaps.keySet()) {
                Object currObj = Optional.ofNullable(list1.get(index)).orElse("");
                Object compObj = Optional.ofNullable(list2.get(index)).orElse("");
                Comparator<Object> objComparator = objComparator(ordering, colSortMaps.getOrDefault(index, true));
                compareChain = compareChain.compare(currObj, compObj, objComparator);
            }
            return compareChain.result();
        };
    }

    private static Comparator<Object> objComparator(Ordering ordering, boolean asc) {
        if (asc) {
            return ordering::compare;
        } else {
            return ordering.reverse()::compare;
        }
    }

    // List实体排序
    @Test
    public void test04() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "Mahesh", 12));
        list.add(new Student(2, "Suresh", 15));
        list.add(new Student(3, "Nilesh", 10));
        list.add(new Student(4, "ZhangSan", 18));
        System.out.println("---Sorting using Comparator by Age---");
        List<Student> collect = list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        collect.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));

        System.out.println("---Sorting using Comparator by Age in reverse order---");
        list.stream().sorted(Comparator.comparing(Student::getAge).reversed())
                .forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));
    }

    @Test
    void test05() {
        String strings = "[\"Saab\", \"Volvo\", \"BMW\",[sub]]";
        String s1 = strings.substring(1);
        System.out.println("---1>>>" + s1);
        String s2 = s1.substring(0, s1.length() - 1);
        System.out.println("---2>>" + s2);
        List strings1 = Splitter.on(",").trimResults().splitToList(s2);
        System.out.println("--3-->>" + strings1);
        JsonArray jsonElements = JsonObjectUtils.parseArray(strings1.get(3));
        System.out.println("--4-->>" + jsonElements.size());
        System.out.println("--4-->>" + jsonElements.get(0).getAsString());
    }

    @Test
    void test06() {
        Set<String> allKeys = new HashSet<>();
        allKeys.add("DEVICE:NETWORK:192.168.1.81");
//        ArrayList<String> strings = Lists.newArrayList("DEVICE:NETWORK:192.168.1.81");
        List<String> strings1 = ListUtils.guavaPartitionList(new ArrayList<>(allKeys), 0, 20);
        System.out.println(strings1);
        ArrayList<String> arrayList2 = Lists.newArrayList("DEVICE:NETWORK:192.168.1.81", "DEVICE:NETWORK:192.168.1.82");
        List<String> strings2 = ListUtils.guavaPartitionList(arrayList2, 0, 20);
        System.out.println("===>>" + strings2);
        List<String> strings3 = ListUtils.guavaPartitionList(arrayList2, 1, 1);
        System.out.println("=--2---->" + strings3);
    }
}
