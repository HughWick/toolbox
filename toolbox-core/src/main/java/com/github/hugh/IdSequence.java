package com.github.hugh;

import com.github.hugh.id.Snowflake;
import com.github.hugh.support.instance.Instance;

/**
 * ID唯一生成器
 *
 * @author hugh
 * @version 1.0.0
 */
public class IdSequence {

    private IdSequence() {
    }

    /**
     * 雪花算法唯一ID
     *
     * @return String 19位数字字符串
     */
    public static String snowflake() {
        var snowflake = Instance.getInstance().singleton(Snowflake.class);
        return String.valueOf(snowflake.nextId());
    }

    /**
     * 雪花算法唯一ID
     *
     * @param dataCenterId 数据中心ID
     * @param machineId    机器ID
     * @return String 19位数字字符串
     * @since 2.4.2
     */
    public static String snowflake(long dataCenterId, long machineId) {
        return String.valueOf(new Snowflake(dataCenterId, machineId).nextId());
    }
}
