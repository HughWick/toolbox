package com.github.hugh.json.model;

import lombok.Data;

import java.util.Date;

/**
 * User: AS
 * Date: 2022/11/1 10:04
 */
@Data
public class ContractsDO {
    public String code;// 合同编号
    public String name;// 项目名称
    public String projectName;// 项目名称
    public String partyA;//甲方名称
    public String partyB;//乙方名称
    public Date useSide;// 使用方
    public String dateOfSigning;// 签订日期
    public String installationAddress;//安装地点
    private Date createDate;//安装地点
}
