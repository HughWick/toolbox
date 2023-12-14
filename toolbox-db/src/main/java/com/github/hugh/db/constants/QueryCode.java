package com.github.hugh.db.constants;

/**
 * 标准的数据库字段常量命令
 *
 * @author hugh
 * @since 2.1.0
 */
public class QueryCode {

    /**
     * 序列号
     */
    public static final String SERIAL_NO = "SERIAL_NO";

    /**
     * 序列号
     */
    public static final String SERIAL_NUMBER = "SERIAL_NUMBER";

    /**
     * 开始日期
     */
    public static final String START_DATE = "START_DATE";

    /**
     * 结束日期
     */
    public static final String END_DATE = "END_DATE";

    /**
     * 删除标识
     */
    public static final String DELETE_FLAG = "DELETE_FLAG";

    /**
     * 库表中的创建日期
     */
    public static final String CREATE_DATE = "CREATE_DATE";

    /**
     * 创建人
     */
    public static final String CREATE_BY = "CREATE_BY";

    /**
     * 状态
     */
    public static final String STATUS = "STATUS";

    /**
     * 标识
     */
    public static final String FLAG = "FLAG";

    /**
     * 名称
     */
    public static final String NAME = "NAME";

    /**
     * 小写
     */
    public static class Lowercase {
        public static final String SERIAL_NO = QueryCode.SERIAL_NO.toLowerCase();
        public static final String SERIAL_NUMBER = QueryCode.SERIAL_NUMBER.toLowerCase();
        public static final String START_DATE = QueryCode.START_DATE.toLowerCase();
        public static final String END_DATE = QueryCode.END_DATE.toLowerCase();
        public static final String DELETE_FLAG = QueryCode.DELETE_FLAG.toLowerCase();
        public static final String CREATE_DATE = QueryCode.CREATE_DATE.toLowerCase();
        public static final String CREATE_BY = QueryCode.CREATE_BY.toLowerCase();
        public static final String STATUS = QueryCode.STATUS.toLowerCase();
        public static final String FLAG = QueryCode.FLAG.toLowerCase();
        public static final String NAME = QueryCode.NAME.toLowerCase();
    }
}
