package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.ListUtils;
import com.github.hugh.util.regex.RegexUtils;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * list 工具测试集合
 *
 * @author AS
 * @date 2020/9/14 9:36
 */
class ListTest {

    // 测试字符串转list集合
    @Test
    void testGuavaStringToList() {
//        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
//        ListUtils.guavaStringToList(strings).forEach(System.out::println);
        String single = "1db";
//        System.out.println("--1->>" + ListUtils.guavaStringToList(single));
        assertEquals(Lists.newArrayList(single), ListUtils.guavaStringToList(single));
        String strings = "[\"Saab\", \"Volvo\", \"BMW\" ,\"   \"]";
        assertEquals("[Saab, Volvo, BMW]", ListUtils.guavaStringToList(strings).toString());
//        System.out.println("-2-->>" + ListUtils.guavaStringToList(strings));
        String str = "{\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\"}";
        assertEquals(Lists.newArrayList("{\"1", "2", "3", "4", "5", "6", "7\"}"), ListUtils.guavaStringToList(str));
//        ListUtils.guavaStringToList(str).forEach(System.out::println);
        String str2 = "1,2 , 3, 4 ,5,6,";
//        System.out.println("-------3333333333----------");
//        ListUtils.guavaStringToList(str2).forEach(System.out::println);
        assertEquals(Lists.newArrayList("1", "2", "3", "4", "5", "6"), ListUtils.guavaStringToList(str2));
        String str3 = "[1,2,3,4,5,]";
        assertEquals(Lists.newArrayList("1", "2", "3", "4", "5"), ListUtils.guavaStringToList(str3));
//        System.out.println("----444444444444----------");
//        ListUtils.guavaStringToList(str3).forEach(System.out::println);
        String str4 = "[1,2,3,4,5,6, \"7\"]";
        assertEquals(Lists.newArrayList("1", "2", "3", "4", "5", "6", "7"), ListUtils.guavaStringToList(str4));
//        System.out.println("======5555555555555555========");
//        ListUtils.guavaStringToList(str4).forEach(System.out::println);
        String str5 = "[[a,b,c],1,2,3,4,5,6, \"7\",89]";
        assertEquals(Lists.newArrayList("a", "b", "c", "1", "2", "3", "4", "5", "6", "7", "89"), ListUtils.guavaStringToList(str5));
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
//        ListUtils.guavaStringToList(str5).;
        String str6 = "[\"Saab\", \"Volvo\", \"BMW\",[sub]]";
        assertEquals(Lists.newArrayList("Saab", "Volvo", "BMW", "sub"), ListUtils.guavaStringToList(str6));
//        System.out.println("--3->>" + ListUtils.guavaStringToList(str2).size());
    }

    //无实体、针对字段排序排序
    @Test
    void testListObjectSort() {
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
        var str1 = "[[刘爱, 男, 22, 9000, T1, 北京大兴], [张三, 男, 22, 10000, T2, 贵州遵义], [王八, 女, 23, 12000, T2, 北京昌平], [王五, 女, 23, 12000, T3, 北京海淀], [张七, 男, 23, 12000, T3, 北京朝阳], [王六, 男, 23, 13000, T3, 北京昌平], [李四, 女, 23, 15000, T2, 贵州遵义], [黄三, 男, 24, 10000, T2, 北京大兴], [黄五, 女, 24, 11000, T2, 北京海淀], [郭艾琳, 男, 24, 12000, T3, 贵州贵阳], [王七, 男, 24, 14000, T3, 北京昌平], [胡三, 男, 26, 12000, T3, 北京朝阳], [胡四, 男, 26, 13000, T3, 北京朝阳], [张五, 女, 26, 14000, T3, 北京海淀], [张六, 男, 27, 17000, T4, 北京朝阳], [李根, 男, 28, 20000, T5, 贵州贵阳], [刘跟, 男, 39, 18000, T4, 贵州遵义]]";
//        System.out.println("-2-->>" + lists.toString());
        assertEquals(str1,lists.toString());
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
    void testListEntitySort() {
        Student student1 = new Student(1, "Mahesh", 12);
        Student student2 = new Student(2, "Suresh", 15);
        Student student3 = new Student(3, "Nilesh", 10);
        Student student4 = new Student(4, "ZhangSan", 18);
        List<Student> list = Lists.newArrayList(student1, student2, student3, student4);
//        System.out.println("---Sorting using Comparator by Age---");
        List<Student> resultList = Lists.newArrayList(student3, student1, student2, student4);
        List<Student> collect = list.stream().sorted(Comparator.comparing(Student::getAge)).collect(Collectors.toList());
        assertEquals(collect, resultList);
//        collect.forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));
        List<Student> result2List = Lists.newArrayList(student4, student2, student1, student3);
//        System.out.println("---Sorting using Comparator by Age in reverse order---");
//        list.stream().sorted(Comparator.comparing(Student::getAge).reversed()).forEach(e -> System.out.println("Id:" + e.getId() + ", Name: " + e.getName() + ", Age:" + e.getAge()));
        List<Student> reverseOrderList = list.stream().sorted(Comparator.comparing(Student::getAge).reversed()).collect(Collectors.toList());
        assertEquals(reverseOrderList, result2List);
    }

//    @Test
//    void test05() {
//        String strings = "[\"Saab\", \"Volvo\", \"BMW\",[sub]]";
//        String s1 = strings.substring(1);
//        System.out.println("---1>>>" + s1);
//        String s2 = s1.substring(0, s1.length() - 1);
//        System.out.println("---2>>" + s2);
//        List strings1 = Splitter.on(",").trimResults().splitToList(s2);
//        System.out.println("--3-->>" + strings1);
////        JsonArray jsonElements = JsonObjectUtils.parseArray(strings1.get(3));
////        System.out.println("--4-->>" + jsonElements.size());
////        System.out.println("--4-->>" + jsonElements.get(0).getAsString());
//    }

    // 测试guava List 分页查询
    @Test
    void testGuavaPartitionList() {
        String str1 = "DEVICE:NETWORK:192.168.1.81";
        Set<String> allKeys = new HashSet<>();
        allKeys.add(str1);
//        ArrayList<String> strings = Lists.newArrayList("DEVICE:NETWORK:192.168.1.81");
        List<String> strings1 = ListUtils.guavaPartitionList(new ArrayList<>(allKeys), 0, 20);
        assertEquals(Collections.singletonList(str1), strings1);
//        System.out.println(strings1);
        ArrayList<String> arrayList2 = Lists.newArrayList(str1, "DEVICE:NETWORK:192.168.1.82");
        List<String> strings2 = ListUtils.guavaPartitionList(arrayList2, 0, 20);
//        System.out.println("===>>" + strings2);
        assertEquals(Arrays.asList(str1, "DEVICE:NETWORK:192.168.1.82"), strings2);
        List<String> strings3 = ListUtils.guavaPartitionList(arrayList2, 1, 1);
//        System.out.println("=--2---->" + strings3);
        assertEquals(Collections.singletonList("DEVICE:NETWORK:192.168.1.82"), strings3);
        assertThrowsExactly(NullPointerException.class, () -> ListUtils.guavaPartitionList(null, 1, 1));
    }

    // list 转字符串测试
    @Test
    void testListToString() {
        List<String> originList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            originList.add(i + "");
        }
        String s = ListUtils.listToString(originList);
        assertEquals("0,1,2,3,4,5,6,7,8,9", s);
        String s2 = ListUtils.listToString(originList, ";");
        assertEquals("0;1;2;3;4;5;6;7;8;9", s2);
        assertEquals("0123456789", ListUtils.listToString(originList, ""));
    }

    // 测试list移除
    @Test
    void testListRemoveIf() {
        List<String> strings = ListUtils.guavaStringToList("136438455@qq.com");
        strings.add("123");
        strings.removeIf(RegexUtils::isNotEmail);
        assertEquals(Lists.newArrayList("136438455@qq.com"), strings);
//        strings.forEach(System.out::println);
    }

    @Test
    void testListToInSql() {
        List<String> list = new ArrayList<>();
        list.add("192.168.1.1");
        list.add("192.168.1.21");
        assertEquals("'192.168.1.1','192.168.1.21'", ListUtils.listToInSql(list));
        Student student = new Student();
        student.setName("张三");
        Student student2 = new Student();
        student2.setName("李四");
        Student student3 = new Student();
        student3.setName("王五");
        Student student4 = new Student();
        student4.setName(" ");
        List<Student> list2 = new ArrayList<>();
        list2.add(student);
        list2.add(student2);
        list2.add(student3);
        list2.add(student4);
        assertEquals("'张三','李四','王五'", ListUtils.listToInSql(list2, "name"));
    }

    // 测试字符串数组直接转in sql语句的字符串
    @Test
    void testStrArrayToInSql() {
        String strArray1 = "a,b,c";
        assertEquals("'a','b','c'", ListUtils.strArrayToInSql(strArray1, ","));
        assertEquals("'a','b','c'", ListUtils.strArrayToInSql(strArray1));
        String strArray2 = "123";
//        ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class, () -> ListUtils.strArrayToInSql(strArray2));
//        assertEquals("unknown separator : " + strArray2, toolboxException.getMessage());
    }

    // list 转字符串
    @Test
    void testListObjectToString() {
        Student student = new Student();
        student.setName("张三");
        Student student2 = new Student();
        student2.setName("李四");
        Student student3 = new Student();
        student3.setName("王五");
        Student student4 = new Student();
        student4.setName(" ");
        List<Student> list = Lists.newArrayList(student, student2, student3, student4);
        assertEquals("张三_李四_王五", ListUtils.listObjectToString(list, "name", "_"));
        assertEquals("张三,李四,王五", ListUtils.listObjectToString(list, "name"));
//        System.out.println();
        List<Map> listMap = new ArrayList<>();
        var item = new HashMap<>();
        item.put("name", "狗蛋");
        listMap.add(item);
        var item2 = new HashMap<>();
        item2.put("name", "钢弹");
        listMap.add(item2);
//        System.out.println(ListUtils.listObjectToString(listMap, "name"));
        assertEquals("狗蛋,钢弹", ListUtils.listObjectToString(listMap, "name"));
    }
}
