package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.TreeNode;
import lombok.Data;

import java.util.List;

/**
 * @author AS
 * @date 2023/10/12 10:36
 */
@Data
public class TreeNodeObject {
    private String provinceCode;//省份编码
    private String provinceName;//省份名称
    private String cityCode;//城市编码
    private String cityName;//城市名称
    private String areaCode;// 区域编码
    private String areaName;// 区域名称
    private String streetCode;// 街道编码
    private String streetName;// 街道名称
    private String streetCodeEx;// 街道编码扩展为8位
    /**
     * 部门下所管辖部门
     */
    private List<TreeNode> children;

}