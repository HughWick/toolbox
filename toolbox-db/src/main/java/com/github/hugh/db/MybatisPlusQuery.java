package com.github.hugh.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.hugh.constant.QueryCode;
import com.github.hugh.db.util.MybatisPlusQueryUtils;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * MybatisPlusQuery 类用于封装 MyBatis-Plus 查询的相关操作。
 * <p>
 * 提供了设置查询条件、分页参数、删除标志、键名大小写等方法。
 *
 * @since 2.8.4
 */
public class MybatisPlusQuery {


    /**
     * 是否将查询条件中的键转换为大写。
     * <p>
     * 默认为 false，表示不转换为大写。
     */
    private boolean keyUpper;

    /**
     * 存储查询条件的映射。
     * <p>
     * 键是条件的名称，值是条件的值。
     */
    private Map<String, Object> item;

    /**
     * 构造方法，初始化 item 为一个空的 HashMap，keyUpper 默认为 false。
     */
    public MybatisPlusQuery() {
        this.item = new HashMap<>();
        this.keyUpper = false;
    }

    /**
     * 根据 HttpServletRequest 中的参数设置查询条件。
     * <p>
     * 会调用 ServletUtils.getParams(request) 方法将请求参数填充到 item 中。
     *
     * @param request HttpServletRequest 请求对象
     * @return 当前 MybatisPlusQuery 实例
     */
    public MybatisPlusQuery request(HttpServletRequest request) {
        // 从 request 中获取参数并设置到 item 中
        this.item = ServletUtils.getParams(request);
        return this;
    }

    /**
     * 移除分页参数。
     * <p>
     * 会从 item 中删除与分页相关的参数，常见的分页参数包括页码和每页大小。
     *
     * @return 当前 MybatisPlusQuery 实例
     */
    public MybatisPlusQuery removePageSize() {
        // 移除分页相关的参数
        MapUtils.removeKeys(this.item, ServletUtils.PAGE_PARAMS);
        return this;
    }

    /**
     * 设置默认的删除标志为 0（假设 0 表示未删除）。
     * <p>
     * 这个方法用于确保查询中默认包含删除标志条件。
     *
     * @return 当前 MybatisPlusQuery 实例
     */
    public MybatisPlusQuery defaultDeleteFlag() {
        // 默认设置删除标志为 0
        this.item.put(QueryCode.Lowercase.DELETE_FLAG, 0);
        return this;
    }

    /**
     * 设置查询条件中的键名转换为大写。
     * <p>
     * 该方法会将查询条件中的所有键转换为大写字母。
     *
     * @return 当前 MybatisPlusQuery 实例
     */
    public MybatisPlusQuery keyUpper() {
        this.keyUpper = true;
        return this;
    }

    /**
     * 处理查询排序和排序字段的默认值设置，并创建一个 MybatisPlus 的 QueryWrapper。
     * <p>
     * 该方法会从当前对象的 `item` 中获取排序方向和排序字段，如果未设置，分别使用默认值：
     * - 排序方向：DESC
     * - 排序字段：ID
     * </p>
     *
     * @param <T> 泛型类型，用于 MybatisPlus 的 QueryWrapper
     * @return 返回一个 MybatisPlus 的 QueryWrapper，已经根据排序方向和排序字段进行了初始化
     */
    public <T> QueryWrapper<T> v1() {
        // 从 item 中获取排序方向和排序字段，若为空，则使用默认值
        String order = String.valueOf(this.item.get(MybatisPlusQueryUtils.ORDER));
        String sort = String.valueOf(this.item.get(MybatisPlusQueryUtils.SORT));
        // 若 order 为空，则设置为 DESC
        if (EmptyUtils.isEmpty(order)) {
            this.item.put(MybatisPlusQueryUtils.ORDER, "DESC");
        }
        // 若 sort 为空，则设置为 ID
        if (EmptyUtils.isEmpty(sort)) {
            this.item.put(MybatisPlusQueryUtils.SORT, "ID");
        }
        return MybatisPlusQueryUtils.createDef(this.item);
    }

    /**
     * 使用当前 item 和 keyUpper 创建一个 MybatisPlus 的 QueryWrapper。
     * <p>
     * 该方法会通过 MybatisPlusQueryUtils 的 create 方法，根据 item 和 keyUpper 构建一个查询条件的包装器。
     * </p>
     *
     * @param <T> 泛型类型，用于 MybatisPlus 的 QueryWrapper
     * @return 返回一个 MybatisPlus 的 QueryWrapper，使用给定的 item 和 keyUpper
     */
    public <T> QueryWrapper<T> create() {
        return MybatisPlusQueryUtils.create(this.item, this.keyUpper);
    }
}
