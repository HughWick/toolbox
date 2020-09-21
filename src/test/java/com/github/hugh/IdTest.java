package com.github.hugh;

/**
 * @author AS
 * @date 2020/9/17 17:36
 */
public class IdTest {

    public static void main(String[] args) {
        System.out.println("-1-->" + IdSequence.snowflake());
        System.out.println("-2-->" + IdSequence.snowflake());
        System.out.println("-3-->" + IdSequence.snowflake());
    }
}
