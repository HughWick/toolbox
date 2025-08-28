package com.github.hugh.bean.expand.cascader;

import lombok.Data;

import java.util.List;

/**
 * element ui 属性空间对应级联组件
 *
 * @author hugh
 * @since 2.8.5
 */
@Data
public class ElementCascader {

    private String label;//节点属性，显示的名称
    private String value;// 选中的code
    private List<ElementCascader> children;//该节点的子节点对象
}
