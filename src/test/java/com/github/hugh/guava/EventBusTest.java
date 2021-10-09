package com.github.hugh.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

/**
 * 测试guava EventBus
 * User: AS
 * Date: 2021/10/9 10:47
 */
class EventBusTest {

    // 测试同步
    @Test
    void testSynEventBus() {
        EventBus eventBus = new EventBus();
        eventBus.register(new EventListener());
        eventBus.post(1);
        System.out.println("--------------");
        eventBus.post(2);
        System.out.println("--------------");
        eventBus.post(3);
        System.out.println("--------------");
        eventBus.post(4);
        System.out.println("=========END====");
    }

    // 测试异步
    @Test
    void testAsynEventBus() {
        EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        eventBus.register(new EventListener());
        eventBus.post(1);
        System.out.println("--------------");
        eventBus.post(2);
        System.out.println("--------------");
        eventBus.post(3);
        System.out.println("--------------");
        eventBus.post(4);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=========END====");
    }
}

class EventListener {

    /**
     * 监听 Integer 类型的消息
     */
    @Subscribe
    public void listenInteger(Integer param) {
        System.out.println("EventListener#listenInteger ->" + param);
        try {
            Thread.sleep(1000);
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


