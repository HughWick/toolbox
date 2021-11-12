package com.github.hugh.id;

import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2021/1/12 9:50
 */
public class UuidTest {


    @Test
    public void test01() {
        System.out.println("-1-->>" + Uuid.genId(12));
        System.out.println("--2->>" + Uuid.genId(12).length());
    }


    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        new Thread(() -> {
//            for (int i = 0; i < 100000; i++) {
//                String id = genId();
//                if (list.contains(id)) {
//                    System.out.println("=============相同uuid====" + id);
//                } else {
//                    list.add(id);
//                }
//            }
//            System.out.println("==1==END====");
//        }).start();
//        new Thread(() -> {
//            for (int i = 0; i < 10000; i++) {
//                String id = genId();
//                if (list.contains(id)) {
//                    System.out.println("=============相同uuid====" + id);
//                } else {
//                    list.add(id);
//                }
//            }
//            System.out.println("==2==END====");
//        }).start();
//        System.out.println("====END====");
//        System.out.println("--->>" + genId());
    }
}
