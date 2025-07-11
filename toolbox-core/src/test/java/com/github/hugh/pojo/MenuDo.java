package com.github.hugh.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

//菜单拓展数据实体类
@NoArgsConstructor
@Data
public class MenuDo {

    private Integer sort;
    private Object icon;
    private Object menuPath;
    private Integer level;
    private String createDate;
    private String createBy;
    private Integer type;
    private Object permissionCode;
    private Long serialNo;
}
