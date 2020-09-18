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
        kryo.setReferences(true);// 默认值为 true, 强调作用
        kryo.setRegistrationRequired(false);// 默认值为 false, 强调作用
        return kryo;
    });

    /**
     * 序列化
     *
     * @param object 参数
     * @return byte[]
     * @since 1.1.4
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
     * @since 1.1.4
     */
    public static Object toObject(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        Kryo kryo = kryoLocal.get();
        return kryo.readClassAndObject(input);
    }
}
