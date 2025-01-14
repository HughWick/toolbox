# toolbox

收集日常工作中常用的工具类

[![Build Status](https://app.travis-ci.com/HughWick/toolbox.svg?branch=2.7.X)](https://app.travis-ci.com/github/HughWick/toolbox)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hughwick/toolbox-all/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hughwick/toolbox-all)
[![](https://img.shields.io/badge/license-Apache2-FF0080.svg)](https://github.com/hughwick/toolbox/blob/master/LICENSE.txt)
[![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/hughwick/toolbox)

## JDK版本

JDK >=11

## 第三方引入依赖

| 依赖                                                    | 版本号        | 说明                |
|--------------------------------------------------------|------------|-------------------|
| validation-api                                         | 2.0.2      | 注解                |
| knife4j                                                | 3.0.3      | Swagger生成Api文档的增强 |
| spring-beans                                           | 5.3.23     | spring 实体类工具      |
| jedis                                                  | 3.6.3      | redis clients     |
| [caffeine](https://github.com/ben-manes/caffeine)      | 3.1.8      | 高性能本地缓存           |
| [okhttp3](https://github.com/square/okhttp)            | 4.9.3      | 高性能http           |
| javax.servlet-api                                      | 4.0.1      | Servlet API       |
| kryo                                                   | 4.0.2      | 快速高效的Java序列化框架    |
| [guava](https://github.com/google/guava)               | 33.1.0-jre | 谷歌套件              |
| [gson](https://github.com/google/gson)                 | 2.10.1     | Google Gson       |
| [fastjson](https://github.com/alibaba/fastjson2)       | 1.2.83     | 阿里高性能json         |
| [slf4j-simple](https://github.com/qos-ch/slf4j)        | 2.0.9      | 日志工具包             |
| [ip2region](https://github.com/lionsoul2014/ip2region) | 2.7.0      | IP地址库             |
| UserAgentUtils                                         | 1.21       | UserAgent解析工具     |
| spring-boot-starter-data-mongodb                       | 2.6.13     | Mongodb           |
| [mybatis-plus-boot-starter](https://www.baomidou.com/) | 3.5.2      | Mybatis plus      |

## maven 引入

```xml
<dependency>
    <groupId>com.github.hughwick</groupId>
    <artifactId>toolbox-all</artifactId>
    <version>2.7.24</version>
</dependency>
```


## Mybatis(Mongodb)查询工具类参数说明

##### 
| key              | value               | 数据类型  | 说明                               |
|------------------|---------------------|----------|----------------------------------|
| serialNo_like    | 123                 | Object   | 模糊查询所有serialNo中包含123的数据          |
| createBy_name_or | 张三                 | Object   | 返回createBy或name中的值包含“张三”的数据      |
| serialNo_in      | a,b,c               | array    | 返回serialNo中的值等于a、b、c的三条数据        |
| cerateDate_ge    | yyyy-MM-dd HH:mm:ss | Date     | 大于等于                             |
| cerateDate_le    | yyyy-MM-dd HH:mm:ss | Date     | 小于等于                             |
| order            | desc                | Object   | 排序方式：     desc、asc               |
| sort             | id                  | Object   | 排序字段名称                           |


