package com.github.hugh.instance;

import com.github.hugh.model.Student;
import com.github.hugh.model.Student1;
import com.github.hugh.support.instance.Instance;

import java.util.concurrent.ExecutionException;


/**
 * @author AS
 * @date 2020/9/21 17:31
 */
public class InstanceTest {

//    @Test
//    public void test() {
//        try {
//            Student singleton = InstanceFactory.getInstance().singleton(Student.class);
//            System.out.println("--1->>" + singleton);
//            Student singleton2 = InstanceFactory.getInstance().singleton(Student.class);
//            System.out.println("--2->>" + singleton2);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("thread1");
            Instance instance = Instance.getInstance();
            System.out.println("thread1 instance : " + instance);
            Student singleton = instance.singleton(Student.class);
            System.out.println("thread1 : " + singleton);
        }).start();

        new Thread(() -> {
            System.out.println("thread2");
            Instance instance = Instance.getInstance();
            System.out.println("thread2 instance : " + instance);
            Student singleton = instance.singleton(Student.class);
            System.out.println("thread2 : " + singleton);
        }).start();

        new Thread(() -> {
            System.out.println("thread3");
            Instance instance = Instance.getInstance();
            System.out.println("thread3 instance : " + instance);
            Student1 singleton = instance.singleton(Student1.class);
            System.out.println("thread3 : " + singleton);
        }).start();

        new Thread(() -> {
            System.out.println("thread4");
            Instance instance = Instance.getInstance();
            System.out.println("thread4 instance : " + instance);
            Student1 singleton = instance.singleton(Student1.class);
            System.out.println("thread4 : " + singleton);
        }).start();
        System.out.println("===END==========");
    }
}
