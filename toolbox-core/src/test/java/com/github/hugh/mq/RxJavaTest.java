package com.github.hugh.mq;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.junit.jupiter.api.Test;

/**
 * 替代{@link com.google.common.eventbus.EventBus}
 *
 * @author Hugh
 * @sine
 **/
public class RxJavaTest {

    @Test
    void test01() {
        // 1. 创建被观察者 Observable 对象
//        Observable<Integer> integerObservable = Observable.create(observableEmitter -> {
//            // 2. 在复写的subscribe（）里定义需要发送的事件
//            // ObservableEmitter类介绍
//            // a. 定义：事件发射器
//            // b. 作用：定义需要发送的事件 & 向观察者发送事件
//            observableEmitter.onNext(1);
//            observableEmitter.onNext(2);
//            observableEmitter.onNext(3);
//            observableEmitter.onComplete();
//        });
        //<--扩展：RxJava 提供了其他方法用于 创建被观察者对象Observable -->
        // 方法1：just(T...)：直接将传入的参数依次发送出来
        Observable<Integer> integerObservable = Observable.just(1, 3);
        // 方法2：from(T[]) / from(Iterable<? extends T>) : 将传入的数组 / Iterable 拆分成具体对象后，依次发送出来
//        String[] words = {"A", "B", "C"};
//        Observable observable = Observable.from(words);
//        integerObservable = Observable.just(7);
        // 2. 创建对象时通过对应复写对应事件方法 从而 响应对应事件
        Observer<Integer> observer = new Observer<>() {

            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("==最先调用==");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {
                System.out.println(value);
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                System.out.println("====complete");
            }
        };
        integerObservable.subscribe(observer);
        System.out.println("========END===");
        integerObservable = Observable.just(7);
        integerObservable.subscribe(observer);
        //注：整体方法调用顺序：观察者.onSubscribe（）> 被观察者.subscribe（）> 观察者.onNext（）>观察者.onComplete()
    }


}
