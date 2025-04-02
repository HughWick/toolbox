package com.github.hugh.bean.expand.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性节点结构实体类
 *
 * @author hugh
 * @since 2.5.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomTreeNode {
    
    private String customLabel;//
    private String customValue;//
}
