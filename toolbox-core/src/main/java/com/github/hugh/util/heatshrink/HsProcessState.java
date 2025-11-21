package com.github.hugh.util.heatshrink;

/**
 * 内部辅助类：用于追踪输入数据的处理进度和状态。
 * <p>
 * 在压缩过程中，这个对象会被重复使用，用来记录当前这批数据
 * 处理到了什么位置（off），还剩余多少字节（len）没有填入滑动窗口。
 * </p>
 */
class HsProcessState {
    /**
     * 当前处理到的数据在数组 b 中的起始偏移量 (offset)。
     * <p>随着数据被写入滑动窗口，这个值会不断增加。</p>
     */
    int off;

    /**
     * 当前待处理数据的剩余长度 (length)。
     * <p>随着数据被写入滑动窗口，这个值会不断减少。</p>
     */
    int len;

    /**
     * 数据处理的结束边界索引 (end index)。
     * <p>通常等于初始的 off + len，用于判断循环是否应该结束。</p>
     */
    int end;

    /**
     * 指向当前正在处理的源数据字节数组 (buffer)。
     * <p>这是一个引用，指向外部传入的原始数据。</p>
     */
    byte[] b;

    /**
     * 初始化或重置数据处理状态。
     *
     * @param b   新的输入数据数组
     * @param off 数据的起始偏移量
     * @param len 数据的长度
     * @return 返回当前 Result 对象实例（支持链式调用）
     */
    HsProcessState set(byte[] b, int off, int len) {
        this.b = b;
        this.off = off;
        this.len = len;
        this.end = off + len; // 计算绝对结束位置，方便循环判断
        return this;
    }
}

