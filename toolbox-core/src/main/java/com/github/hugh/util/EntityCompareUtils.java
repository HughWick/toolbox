package com.github.hugh.util;

import com.github.hugh.bean.dto.EntityCompareResult;
import com.github.hugh.components.entity.EntityCompare;
import com.github.hugh.exception.ToolboxException;

import java.util.List;

/**
 * 实体属性
 *
 * @author hugh
 * @since 2.5.6
 */
public class EntityCompareUtils {
    private EntityCompareUtils() {
    }

    /**
     * 比较两个对象是否不同，忽略指定属性。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 需要忽略比较的属性列表
     * @return 如果两个对象不同，返回 true；否则，返回 false。
     */
    public static boolean isObjDiff(Object obj1, Object obj2, String... ignoreProperties) {
        return ListUtils.isNotEmpty(compare(obj1, obj2, ignoreProperties));
    }

    /**
     * 判断两个对象是否相同，并忽略指定属性。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 需要忽略比较的属性列表
     * @return 如果两个对象相同，返回 true；否则，返回 false。
     */
    public static boolean isObjSame(Object obj1, Object obj2, String... ignoreProperties) {
        return ListUtils.isEmpty(compare(obj1, obj2, ignoreProperties));
    }

    /**
     * 比较两个对象的属性，返回它们之间的差异信息。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 忽略比较的属性
     * @return 两个对象之间的差异信息列表
     */
    public static List<EntityCompareResult> compare(Object obj1, Object obj2, String... ignoreProperties) {
        try {
            // 比较两个对象的属性
            return EntityCompare.on(obj1, obj2, ignoreProperties).compareProperties();
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }
}
