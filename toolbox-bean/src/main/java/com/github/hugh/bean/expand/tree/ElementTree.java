package com.github.hugh.bean.expand.tree;

import lombok.Data;

import java.util.List;

/**
 * element ui 属性空间对应的属性实体类
 *
 * @author hugh
 * @since 2.5.11
 */
@Data
public class ElementTree {

    private String id;//节点位移标识

    private String parentId;//父节点id

    private String label;//节点属性，按需求可定义多个属性

    private List<ElementTree> children;//该节点的子节点对象
}
