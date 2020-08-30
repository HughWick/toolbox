package com.github.hugh;

import com.github.hugh.id.Snowflake;

/**
 * ID唯一生成器
 *
 * @author hugh
 * @version 1.0.0
 */
public class IdSequence {

    public IdSequence() {
    }

    private static volatile Snowflake snowflake;

    private static Snowflake getSnowflake() {
        if (snowflake == null) {//懒汉式
            synchronized (Snowflake.class) {
                if (snowflake == null) {//二次检查
                    snowflake = new Snowflake();
                }
            }
        }
        return snowflake;
    }

    /**
     * 雪花算法唯一ID
     *
     * @return String 19位数字字符串
     */
    public static String snowflake() {
        return String.valueOf(getSnowflake().nextId());
    }

}
