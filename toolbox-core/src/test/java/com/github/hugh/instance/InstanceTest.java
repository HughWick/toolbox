package com.github.hugh.instance;

import com.github.hugh.model.Student;
import com.github.hugh.support.instance.Instance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 测试单列
 *
 * @author AS
 * @date 2020/9/21 17:31
 */
class InstanceTest {

    private Instance instance;

    @BeforeEach
    public void setUp() {
        this.instance = new Instance();
    }
//
//    @Test
//    void testSingleton() {
//        // 测试单例是否正确创建
//        SingletonA a1 = instance.singleton(SingletonA.class);
//        SingletonA a2 = instance.singleton(SingletonA.class);
//        Assertions.assertSame(a1, a2, "SingletonA should have the same instance");
//
//        // 测试是否能正常创建多个不同类的实例
//        SingletonB b1 = instance.singleton(SingletonB.class);
//        SingletonC c1 = instance.singleton(SingletonC.class);
//        Assertions.assertNotNull(b1, "SingletonB should not be null");
//        Assertions.assertNotNull(c1, "SingletonC should not be null");
//    }

//    static class SingletonA {
//    }
//
//    static class SingletonB {
//    }
//
//    static class SingletonC {
//    }

//    @Test
//    void testSingleton2() {
//        new Thread(() -> {
//            System.out.println("thread1");
//            Instance instance = Instance.getInstance();
////            System.out.println("thread1 instance : " + instance);
//            Student singleton = instance.singleton(Student.class);
//            System.out.println("thread1 : " + singleton);
//            Instance.SINGLETON_CACHE.invalidate(Student.class.getName());
//
//            try {
//                System.out.println("--->>" + Instance.SINGLETON_CACHE.get(Student.class.getName()));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        new Thread(() -> {
//            System.out.println("thread2");
//            Instance instance = Instance.getInstance();
////            System.out.println("thread2 instance : " + instance);
//            Student singleton = instance.singleton(Student.class);
//            System.out.println("thread2 : " + singleton);
//        }).start();
//        new Thread(() -> {
//            System.out.println("thread5");
//            Instance instance = Instance.getInstance();
//            Student singleton = instance.singleton(Student.class);
//            System.out.println("thread5 : " + singleton);
//        }).start();
//
//        new Thread(() -> {
//            System.out.println("thread3");
//            Instance instance = Instance.getInstance();
////            System.out.println("thread3 instance : " + instance);
//            Student1 singleton = instance.singleton(Student1.class);
//            System.out.println("thread3 : " + singleton);
//        }).start();
//
//        new Thread(() -> {
//            System.out.println("thread4");
//            Instance instance = Instance.getInstance();
////            System.out.println("thread4 instance : " + instance);
//            Student1 singleton = instance.singleton(Student1.class);
//            System.out.println("thread4 : " + singleton);
//        }).start();
//        System.out.println("===END==========");
//    }

    /**
     * 测试单例模式是否正确创建
     */
    @Test
    void testSingleton2() throws InterruptedException {
        int threadCount = 5; // 线程数量
        CountDownLatch latch1 = new CountDownLatch(1); // 等待所有线程启动
        CountDownLatch latch2 = new CountDownLatch(threadCount); // 等待所有线程结束

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    latch1.await(); // 等待所有线程启动
                    Student s1 = instance.singleton(Student.class);
//                    System.out.println(Thread.currentThread().getName() + ": " + s1);
                    // 随机等待一段时间
                    Thread.sleep((long) (Math.random() * 200));
                    Student s2 = instance.singleton(Student.class);
//                    System.out.println(Thread.currentThread().getName() + ": " + s2);
                    Assertions.assertEquals(s1, s2); // 验证获取到的实例是否相同
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch2.countDown(); // 计数器减1
                }
            }).start();
        }

        latch1.countDown(); // 启动所有线程
        latch2.await(); // 等待所有线程结束
    }

}
