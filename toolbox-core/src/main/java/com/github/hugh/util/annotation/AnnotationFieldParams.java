package com.github.hugh.util.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * AnnotationFieldParams 类用于封装与注解相关的字段和方法信息。
 * <p>
 * 该类包含了一个对象实例、方法名、注解类和字段名，通常用于与反射相关的操作，
 * 比如检查某个对象的指定方法是否带有特定注解，或者在某个对象中查找指定的字段。
 * 该类还提供了一个 `isValid()` 方法，用于验证封装的数据是否有效。
 * </p>
 * <p>
 * 示例用途：
 * - 可以用于动态获取带有指定注解的方法或字段信息。
 * - 用于校验某个字段或方法是否符合预定的条件。
 * </p>
 *
 * @author hugh
 * @since 2.7.20
 */
@AllArgsConstructor
@Data
public class AnnotationFieldParams {
    private Object obj;  // 需要操作的对象实例
    private String methodName;  // 方法名
    private Class<? extends Annotation> annotationClass;  // 注解类类型（继承自Annotation的类）
    private String fieldName;  // 字段名

    /**
     * 检查当前AnnotationFieldParams对象是否有效。
     * 只有在以下条件下才认为该对象有效：
     * - obj 不为 null
     * - methodName 不为 null 且不为空
     * - annotationClass 不为 null
     * - fieldName 不为 null 且不为空
     *
     * @return 如果对象有效返回 true，否则返回 false
     */
    public boolean isValid() {
        // 判断对象是否有效：确保所有字段都不为空或不为null
        return obj != null && methodName != null && !methodName.isEmpty() && annotationClass != null && fieldName != null && !fieldName.isEmpty();
    }

}
