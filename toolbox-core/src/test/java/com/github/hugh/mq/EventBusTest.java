package com.github.hugh.mq;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试guava EventBus
 * User: AS
 * Date: 2021/10/9 10:47
 */
class EventBusTest {

    // 测试同步 有序的
    @Test
    void testSynEventBus() {
        EventBus eventBus = new EventBus();
        eventBus.register(new EventListener());
        int count = 0;
        eventBus.post(count);
        count++;
        eventBus.post(count);
        count++;
        eventBus.post(count);
        count++;
        eventBus.post(count);
    }

    // 测试异步 无序
    @Test
    void testAsynEventBus() {
        EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        eventBus.register(new EventListener());
        eventBus.post("a");
//        System.out.println("--------------");
        eventBus.post("b");
//        System.out.println("--------------");
        eventBus.post("c");
//        System.out.println("--------------");
        eventBus.post("d");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("=========END====");
    }
}

class EventListener {
    private static int count = 0;

    /**
     * 监听 Integer 类型的消息
     */
    @Subscribe
    public void listenInteger(Integer param) {
        if (count == 0) {
            assertEquals(0, param);
        } else if (count == 1) {
            assertEquals(1, param);
        }else if (count == 2) {
            assertEquals(2, param);
        }else if (count == 3) {
            assertEquals(3, param);
        }
        try {
            Thread.sleep(500);
            count++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听 String 类型的消息
     */
    @Subscribe
    public void listenString(String param) {
        System.out.println("EventListener#listenString ->" + param);
    }
}


