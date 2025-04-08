package com.github.hugh.bean.expand.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * element ui 属性空间对应的属性实体类
 *
 * @author hugh
 * @since 2.5.11
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ElementTree extends BaseTreeNode<ElementTree> {

    private String label;//节点属性，按需求可定义多个属性

}
