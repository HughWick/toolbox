package com.github.hugh.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

//菜单拓展数据实体类
@NoArgsConstructor
@Data
public class MenuDo {

    @JsonProperty("sort")
    private Integer sort;
    @JsonProperty("icon")
    private Object icon;
    @JsonProperty("menuPath")
    private Object menuPath;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("createDate")
    private String createDate;
    @JsonProperty("createBy")
    private String createBy;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("permissionCode")
    private Object permissionCode;
    @JsonProperty("serialNo")
    private Long serialNo;
}
