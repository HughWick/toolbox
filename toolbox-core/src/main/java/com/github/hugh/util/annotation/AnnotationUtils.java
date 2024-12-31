package com.github.hugh.util.annotation;

import com.github.hugh.exception.ToolboxException;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类，提供操作注解的通用方法。
 *
 * @author hugh
 * @since 2.7.20
 */
@Slf4j
public class AnnotationUtils {
    private AnnotationUtils() {
    }

    /**
     * 从指定对象的方法中获取指定注解的指定字段值。
     * <p>
     * 该方法通过反射获取方法上的注解，并尝试调用注解的指定字段方法来获取其值。
     * 如果方法上没有该注解，或者注解没有该字段方法，则返回 null。
     * </p>
     *
     * @param params 目标对象，不能为空。
     * @return 注解指定字段的值，如果方法上没有该注解或注解没有该字段方法，或者参数为空，则返回 null。
     * @throws ToolboxException         如果发生反射异常，则抛出该自定义异常。
     * @throws NullPointerException     如果 obj、methodName、annotationClass或fieldName 为 null。
     * @throws IllegalArgumentException 如果 methodName或fieldName 为空字符串。
     */
    public static String getAnnotationField(AnnotationFieldParams params) {
        // 参数验证失败时直接返回 null，并记录日志
        if (params == null || !params.isValid()) {
            logInvalidParams(params);
            return null;
        }
        try {
            Class<?> cls = params.getObj().getClass();
            Method method = cls.getMethod(params.getMethodName());
            if (method.isAnnotationPresent(params.getAnnotationClass())) {
                Annotation annotation = method.getAnnotation(params.getAnnotationClass());
                Method fieldMethod = params.getAnnotationClass().getMethod(params.getFieldName());
                Object value = fieldMethod.invoke(annotation);
                return value != null ? value.toString() : null;
            } else {
                log.debug("方法：{}，上没有找到注解：{}", params.getMethodName(), params.getAnnotationClass().getName());
                return null;
            }
        } catch (NoSuchMethodException noSuchMethodException) {
            log.warn("方法：{}，不存在或注解：{}，没有：{} 字段方法：{}", params.getMethodName(), params.getAnnotationClass().getName(), params.getFieldName(), noSuchMethodException.getMessage());
            throw new ToolboxException(noSuchMethodException.getMessage());
        } catch (ReflectiveOperationException reflectiveOperationException) {
            log.error("反射操作异常：{}", reflectiveOperationException.getMessage(), reflectiveOperationException);
            throw new ToolboxException(reflectiveOperationException.getMessage());
        }
    }

    /**
     * 用于记录参数无效的日志信息。
     *
     * @param params 注解字段参数
     */
    private static void logInvalidParams(AnnotationFieldParams params) {
        if (params == null) {
            log.warn("AnnotationFieldParams 不能为空");
            return;
        }
        if (params.getObj() == null) {
            log.warn("目标对象 obj 不能为空");
        }
        if (params.getMethodName() == null || params.getMethodName().isEmpty()) {
            log.warn("方法名 methodName 不能为空或空字符串");
        }
        if (params.getAnnotationClass() == null) {
            log.warn("注解类 annotationClass 不能为空");
        }
        if (params.getFieldName() == null || params.getFieldName().isEmpty()) {
            log.warn("注解字段名 fieldName 不能为空或空字符串");
        }
    }
}
