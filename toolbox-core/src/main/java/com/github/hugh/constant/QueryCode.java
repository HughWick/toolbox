package com.github.hugh.constant;

/**
 * 标准的数据库字段常量命令
 *
 * @author hugh
 * @since 2.7.0
 */
public class QueryCode {
    private QueryCode() {
    }

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
     *
     * @since 2.7.1
     */
    public static final String NAME = "NAME";
    /**
     * 常量：页码。
     *
     * @since 2.7.1
     */
    public static final String PAGE = "PAGE";

    /**
     * 常量：每页的数据量。
     */
    public static final String SIZE = "SIZE";

    /**
     * app key
     *
     * @since 2.7.15
     */
    public static final String APPKEY = "appkey";

    /**
     * 小写
     *
     * @since 2.7.0
     */
    public static class Lowercase {
        private Lowercase() {
        }

        public static final String SERIAL_NO = QueryCode.SERIAL_NO.toLowerCase();
        /**
         * 序列号
         */
        public static final String SERIAL_NUMBER = QueryCode.SERIAL_NUMBER.toLowerCase();
        /**
         * 开始日期
         */
        public static final String START_DATE = QueryCode.START_DATE.toLowerCase();
        /**
         * 结束日期
         */
        public static final String END_DATE = QueryCode.END_DATE.toLowerCase();
        /**
         * 删除标识
         */
        public static final String DELETE_FLAG = QueryCode.DELETE_FLAG.toLowerCase();
        /**
         * 库表中的创建日期
         */
        public static final String CREATE_DATE = QueryCode.CREATE_DATE.toLowerCase();
        /**
         * 创建人
         */
        public static final String CREATE_BY = QueryCode.CREATE_BY.toLowerCase();
        /**
         * 状态
         */
        public static final String STATUS = QueryCode.STATUS.toLowerCase();
        /**
         * 标识
         */
        public static final String FLAG = QueryCode.FLAG.toLowerCase();
        /**
         * 名称
         */
        public static final String NAME = QueryCode.NAME.toLowerCase();
        /**
         * 常量：页码。
         *
         * @since 2.7.1
         */
        public static final String PAGE = QueryCode.PAGE.toLowerCase();
        /**
         * 常量：每页的数据量。
         *
         * @since 2.7.1
         */
        public static final String SIZE = QueryCode.SIZE.toLowerCase();

        /**
         * 常量：app key
         *
         * @since 2.7.15
         */
        public static final String APPKEY = QueryCode.APPKEY.toLowerCase();

    }
}
