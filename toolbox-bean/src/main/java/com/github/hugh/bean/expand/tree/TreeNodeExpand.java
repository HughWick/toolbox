package com.github.hugh.bean.expand.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 属性节点结构额外拓展实体类
 *
 * @author hugh
 * @since 2.6.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNodeExpand<T> {

    private String id;//节点位移标识
    private String parentId;//父节点id
    private String value;//节点属性，按需求可定义多个属性
    private List<TreeNodeExpand<T>> children;//该节点的子节点对象
    private T expand;// 拓展字段
}
