package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 序列化辅助类
 *
 * @author hugh
 */
public final class SerializeUtils {
    private SerializeUtils() {
    }

    /**
     * <p>
     * Kryo 支持对注册行为，如 kryo.register(SomeClazz.class);
     * </p>
     * <p>
     * 这会赋予该 Class 一个从 0 开始的编号，但 Kryo 使用注册行为最大的问题在于，其不保证同一个 Class 每一次注册的号码想用，这与注册的顺序有关，也就意味着在不同的机器、同一个机器重启前后都有可能拥有不同的编号，这会导致序列化产生问题，所以在分布式项目中，一般关闭注册行为。
     * </p>
     * <p>
     * 第二个注意点在于循环引用，Kryo 为了追求高性能，可以关闭循环引用的支持。不过我并不认为关闭它是一件好的选择，大多数情况下，请保持 kryo.setReferences(true)。
     * </p>
     */
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        /**
         * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
         * 上线的同时就必须清除 Redis 里的所有缓存，
         * 否则那些缓存再回来反序列化的时候，就会报错
         */
        // 支持对象循环引用（否则会栈溢出）
        kryo.setReferences(true);//默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
        // 不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        kryo.setRegistrationRequired(false);//默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
        return kryo;
    });

    /**
     * 序列化
     *
     * @param object 参数
     * @return byte[]
     * @since 1.2.0
     */
    public static <T> byte[] toBytes(T object) {
        Kryo kryo = kryoLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @return T 对象
     * @since 1.2.0
     */
    public static Object toObject(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        Kryo kryo = kryoLocal.get();
        return kryo.readClassAndObject(input);
    }
}
