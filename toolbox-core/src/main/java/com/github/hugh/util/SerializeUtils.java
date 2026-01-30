package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;

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
        // 设置实例化策略，支持无参构造函数的类序列化（很多第三方库的类没有无参构造）
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    /**
     * 序列化
     *
     * @param <T>    实体类型
     * @param object 参数
     * @return byte[]
     * @since 1.2.0
     */
    public static <T> byte[] toBytes(T object) {
        if (object == null) {
            return null;
        }
        Kryo kryo = kryoLocal.get();
        // 初始 buffer 大小 4KB，最大不限制（-1）
        // 直接使用 Output 内部的 byte 数组，避免创建 ByteArrayOutputStream
        try (Output output = new Output(4096, -1)) {
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @return T 对象
     * @since 1.2.0
     */
    public static Object toObject(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        Kryo kryo = kryoLocal.get();
        return kryo.readClassAndObject(input);
    }

    /**
     * 反序列化 (指定 Class 类型)
     * <p>
     * 注意：由于序列化时使用的是 writeClassAndObject，这里即便传入 Class，
     * Kryo 依然会读取流中的类头信息。此方法的 Class 参数主要用于类型校验和泛型转换。
     *
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类型的 Class 对象
     * @param <T>   泛型
     * @return 反序列化后的对象
     * @since 3.0.21
     */
    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        Object obj = toObject(bytes);
        if (obj == null) {
            return null;
        }
        if (clazz != null && !clazz.isInstance(obj)) {
            throw new ClassCastException("反序列化对象类型不匹配。期望: " + clazz.getName() + ", 实际: " + obj.getClass().getName());
        }
        return clazz.cast(obj);
    }

    /**
     * 防止 ThreadLocal 内存泄漏
     * 建议在 web 容器的过滤器或拦截器的 finally 块中调用
     *
     * @since 3.0.21
     */
    public static void remove() {
        kryoLocal.remove();
    }
}
