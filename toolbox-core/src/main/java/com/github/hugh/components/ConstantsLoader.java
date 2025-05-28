package com.github.hugh.components;

import com.github.hugh.exception.ConstantsLoadException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 用于通过反射加载常量类中String类型静态final字段值的工具类。
 *
 * @since 3.0.4
 */
public class ConstantsLoader {

    /**
     * 允许开发者自定义字段过滤条件，加载指定类中符合任意条件的字段值。
     *
     * @param className    包含常量的类的完整路径名。
     * @param fieldFilter  用于过滤字段的谓词 (Predicate)。只有满足此条件的字段才会被处理。
     * @param expectedType 期望加载的字段值的类型。如果为 null，则不进行类型检查。
     * @param <T>          字段值的类型。
     * @return 包含符合条件字段值的不可修改Set。
     * @throws ConstantsLoadException 如果在加载过程中发生错误（如类未找到、访问权限问题、类型不匹配等）。
     */
    public static <T> Set<T> load(String className, Predicate<Field> fieldFilter, Class<T> expectedType) {
        Set<T> constantsSet = new HashSet<>();
        try {
            Class<?> constantsClass = Class.forName(className);
            for (Field field : constantsClass.getDeclaredFields()) {
                // 应用外部传入的字段过滤器
                if (fieldFilter.test(field)) {
                    // 如果指定了期望类型，则进行类型检查
                    if (expectedType != null && !expectedType.isAssignableFrom(field.getType())) {
                        continue; // 类型不匹配则跳过此字段
                    }
                    // 尝试获取字段值
                    try {
                        // 对于非公共字段，需要设置可访问性，但如果公共字段过滤器已生效，则此处只针对特殊场景
                        // 如果fieldFilter中没有包含Modifier.isPublic()的检查，这里就可能抛出IllegalAccessException
                        // 为了避免强制设为可访问性而增加风险，我们依赖fieldFilter来保证字段是可访问的。
                        // 如果你明确需要访问非公共字段，请在 fieldFilter 中移除 Modifier.isPublic() 检查，并在此处手动设置 field.setAccessible(true)。
                        // 通常情况下，我们认为仅加载 public 字段是更安全的。
                        constantsSet.add(expectedType != null ? expectedType.cast(field.get(null)) : (T) field.get(null));
                    } catch (IllegalAccessException illegalAccessException) {
                        // 即使字段被认为是公共的，也可能因为SecurityManager或其他JVM限制导致IllegalAccessException
                        // 记录详细信息或重新抛出，让调用方知道具体问题
                        throw new ConstantsLoadException("Illegal access to field '" + field.getName() + "' in class '" + className + "'. Ensure field is public or setAccessible(true) is used if private access is intended.", illegalAccessException);
                    }
                }
            }
        } catch (ClassNotFoundException classNotFoundException) {
            throw new ConstantsLoadException("Class not found during constants loading: " + className, classNotFoundException);
        } catch (ClassCastException classCastException) { // 如果强制转换失败 (针对expectedType.cast())
            throw new ConstantsLoadException("Type mismatch during constant loading for type " + (expectedType != null ? expectedType.getName() : "unknown") + " from class: " + className, classCastException);
        } catch (ConstantsLoadException constantsLoadException) {
            // 重新抛出内部 IllegalAccessException 包装的 ConstantsLoadException
            throw constantsLoadException;
        } catch (Exception e) { // 捕获其他可能的反射异常
            throw new ConstantsLoadException("Unexpected error during constants loading from class: " + className, e);
        }
        return Collections.unmodifiableSet(constantsSet);
    }

    /**
     * 加载指定类中所有公共 (public)、静态 (static)、final 且为 String 类型的字段值。
     * 这是最常见的常量加载场景。
     *
     * @param className 包含常量的类的完整路径名。
     * @return 包含所有公共静态 final String 字段值的不可修改Set。
     * @throws ConstantsLoadException 如果在加载过程中发生错误。
     */
    public static Set<String> loadPublicStaticFinalStringConstants(String className) {
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers());
        return load(className, filter, String.class);
    }

    /**
     * 加载指定类中所有公共 (public)、静态 (static)、final 且为指定类型的字段值。
     *
     * @param <T>        字段值的类型。
     * @param className  包含常量的类的完整路径名。
     * @param typeFilter 期望加载的字段类型 Class 对象 (e.g., String.class, Integer.class)。
     * @return 包含所有公共静态 final 指定类型字段值的不可修改Set。
     * @throws ConstantsLoadException 如果在加载过程中发生错误。
     */
    public static <T> Set<T> loadPublicStaticFinalConstants(String className, Class<T> typeFilter) {
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers());
        return load(className, filter, typeFilter);
    }

    /**
     * 加载指定类中所有静态 (static) 且为 String 类型的字段值（无论是否 public 或 final）。
     * 提供更灵活的选择，但使用时需谨慎。
     *
     * @param className 包含常量的类的完整路径名。
     * @return 包含所有静态 String 字段值的不可修改Set。
     * @throws ConstantsLoadException 如果在加载过程中发生错误。
     */
    public static Set<String> loadStaticString(String className) {
        Predicate<Field> filter = field -> Modifier.isStatic(field.getModifiers());
        return load(className, filter, String.class);
    }

    /**
     * 提供一个通用的方法，允许开发者直接传入 Class 对象而不是类名字符串，增强使用便利性。
     * 此方法默认加载公共、静态、final 的指定类型字段。
     *
     * @param <T>            字段值的类型。
     * @param constantsClass 包含常量的 Class 对象。
     * @param typeFilter     期望加载的字段类型 Class 对象。
     * @return 包含所有公共静态 final 指定类型字段值的不可修改Set。
     * @throws ConstantsLoadException 如果在加载过程中发生错误（如 ClassNotFoundException 已被此处 Class 对象传入代替，但其他反射异常仍可能发生）。
     */
    public static <T> Set<T> loadPublicStaticFinalConstants(Class<?> constantsClass, Class<T> typeFilter) {
        Set<T> constantsSet = new HashSet<>();
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers());
        try {
            for (Field field : constantsClass.getDeclaredFields()) {
                if (filter.test(field) && typeFilter.isAssignableFrom(field.getType())) {
                    constantsSet.add(typeFilter.cast(field.get(null)));
                }
            }
        } catch (IllegalAccessException illegalAccessException) {
            throw new ConstantsLoadException("Illegal access to fields in class during constants loading: " + constantsClass.getName(), illegalAccessException);
        } catch (ClassCastException classCastException) {
            throw new ConstantsLoadException("Type mismatch during constant loading for type " + typeFilter.getName() + " from class: " + constantsClass.getName(), classCastException);
        } catch (Exception e) {
            throw new ConstantsLoadException("Unexpected error during constants loading from class: " + constantsClass.getName(), e);
        }
        return Collections.unmodifiableSet(constantsSet);
    }
}
