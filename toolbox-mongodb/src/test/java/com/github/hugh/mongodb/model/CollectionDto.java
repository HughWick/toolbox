package com.github.hugh.mongodb.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author AS
 * @date 2023/12/14 15:08
 */
@Data
@Document("collection_test")
public class CollectionDto {
    private String id;//
    @Field("serial_no")
    private String serialNo;//
    @Indexed
    @Field("serial_number")
    private String serialNumber;// 主机序列号

    @Field("flag")
    private int flag;// 交直流状态：0-交流异常、1-直流异常
    @Field("status")
    private int status;// 上\下电标识：0-掉电、1-上电
    @Field("reported_date")
    private Date reportedDate;//上\下电 上报日期
    @Field("create_by")
    public String createBy;//创建人
    @Field("create_date")
    public Date createDate;//创建时间
    @Field("delete_flag")
    public int deleteFlag;//删除标识：0-未删除、1-已删除
    @Field("data_version")
    public int dataVersion;// 数据类型：0-李工旧版主机、1-自研主机
}
