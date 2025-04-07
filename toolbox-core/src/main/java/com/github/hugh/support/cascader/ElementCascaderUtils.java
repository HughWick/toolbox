package com.github.hugh.support.cascader;

import com.github.hugh.bean.expand.cascader.ElementCascader;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ElementCascaderUtils {

    /**
     * 检查给定的 ElementCascader 列表及其所有子节点中是否存在重复的 value 值。
     * <p>
     * 该方法遍历列表中的每个 ElementCascader 对象，并递归地检查其子节点。
     * 它使用一个 HashSet 来跟踪已经遇到的 value 值。如果在遍历过程中发现相同的 value 值，则认为存在重复。
     *
     * @param elements 要检查的 ElementCascader 对象列表。
     * @return 如果在列表及其子节点中找到任何重复的 value 值，则返回 true；否则返回 false。
     * @since 2.8.6
     */
    public static boolean hasDuplicateValue(List<ElementCascader> elements) {
        Set<String> seenValues = new HashSet<>();
        // 遍历 list 中的每个 ElementCascader 对象，检查 value 是否重复
        for (ElementCascader element : elements) {
            if (hasDuplicateValueRecursive(element, seenValues)) {
                return true; // 如果找到重复的 value，则返回 true
            }
        }
        return false; // 没有找到重复值
    }

    /**
     * 递归地检查 ElementCascader 对象及其子节点是否存在重复的 value 值。
     *
     * @param element    当前要检查的 ElementCascader 对象。
     * @param seenValues 用于存储已经遇到的 value 值的 Set 集合，用于检测重复。
     * @return 如果在当前节点或其任何子节点中找到重复的 value 值，则返回 true；否则返回 false。
     * @since 2.8.6
     */
    private static boolean hasDuplicateValueRecursive(ElementCascader element, Set<String> seenValues) {
        if (element == null) {
            return false;
        }
        // 检查当前节点的 value 是否已经存在
        if (seenValues.contains(element.getValue())) {
            // 如果已经出现过相同的 value，记录日志
            log.error("Duplicate value found: label = {}, value = {}", element.getLabel(), element.getValue());
            return true; // 如果已经出现过相同的 value，返回 true
        }
        // 将当前节点的 value 添加到 seenValues 中
        seenValues.add(element.getValue());
        // 如果有 children，递归检查子节点
        if (element.getChildren() != null) {
            for (ElementCascader child : element.getChildren()) {
                if (hasDuplicateValueRecursive(child, seenValues)) {
                    return true; // 如果子节点中有重复的 value，返回 true
                }
            }
        }
        return false; // 没有重复的 value
    }
}
