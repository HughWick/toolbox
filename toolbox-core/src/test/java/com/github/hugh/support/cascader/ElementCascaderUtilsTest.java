package com.github.hugh.support.cascader;

import com.alibaba.fastjson.JSONArray;
import com.github.hugh.bean.dto.RegionDto;
import com.github.hugh.bean.expand.cascader.ElementCascader;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.support.tree.TreeNodeOpe;
import com.github.hugh.support.tree.TreeNodeOpeTest;
import com.github.hugh.util.TreeNodeUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ElementCascaderUtilsTest {


    @Test
    void testDuplicateValue() {
        List<TreeNode> rootList = new ArrayList<>();
        List<TreeNode> childList = new ArrayList<>();
        List<RegionDto> regionDtos = JSONArray.parseArray(TreeNodeOpeTest.json_data2, RegionDto.class);

        TreeNodeUtils.processCustomThree(regionDtos, rootList, childList);
        TreeNodeOpe<TreeNode, ElementCascader> treeNodeOpe = new CascaderOpe(rootList, childList);
        treeNodeOpe.setMappingType(CascaderOpe.CUSTOM_MAPPING);
        List<ElementCascader> elementCascaderList = treeNodeOpe.processElement();
        assertFalse(ElementCascaderUtils.hasDuplicateValue(elementCascaderList));
    }

}
