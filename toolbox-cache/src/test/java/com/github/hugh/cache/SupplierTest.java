package com.github.hugh.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Hugh
 * @since
 **/
public class SupplierTest {
    public static void main(String[] args) throws InterruptedException {

        Supplier<User> userInfoSu = () -> new User("kobe");


        Supplier memoizeserInfo = Suppliers.memoize(userInfoSu);
        User first = (User) memoizeserInfo.get();

        Thread.currentThread().sleep(2000);

        User second = (User) memoizeserInfo.get();
        if (first == second) {
            System.out.println("same object");
        } else {
            System.out.println("diff object");
        }

    }

    @Data
    @AllArgsConstructor
    static class User {
        private String name;
    }
}