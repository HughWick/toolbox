package com.github.hugh.aop;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * 端口注解测试类
 */
public class PortTest {

    @Test
    void testPort() throws ClassNotFoundException {
        Class<?> cls = Class.forName("com.zbz.annotation.pattern3.Worker");
        Method[] method = cls.getMethods();
        /**判断Worker类上是否有TypeAnnotation注解*/
//        boolean flag = cls.isAnnotationPresent(TypeAnnotation.class);
//        /**获取Worker类上的TypeAnnotation注解值*/
//        if (flag) {
//            TypeAnnotation typeAnno = (TypeAnnotation) cls.getAnnotation(TypeAnnotation.class);
//            System.out.println("@TypeAnnotation值:" + typeAnno.value());
//        }
//
//        /**方法上注解*/
//        List<Method> list = new ArrayList<Method>();
//        for (int i = 0; i < method.length; i++) {
//            list.add(method[i]);
//        }
//
//        for (Method m : list) {
//            MethodAnnotation methodAnno = m.getAnnotation(MethodAnnotation.class);
//            if (methodAnno == null)
//                continue;
//            System.out.println("方法名称:" + m.getName());
//            System.out.println("方法上注解name = " + methodAnno.name());
//            System.out.println("方法上注解url = " + methodAnno.url());
//        }
//        /**属性上注解*/
//        List<Field> fieldList = new ArrayList<Field>();
//        for (Field f : cls.getDeclaredFields()) {// 访问所有字段
//            FiledAnnotation filedAno = f.getAnnotation(FiledAnnotation.class);
//            System.out.println("属性名称:" + f.getName());
//            System.out.println("属性注解值FiledAnnotation = " + filedAno.value());
//        }
    }
}
