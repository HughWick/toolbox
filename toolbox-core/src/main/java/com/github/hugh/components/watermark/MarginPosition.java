package com.github.hugh.components.watermark;

import net.coobird.thumbnailator.geometry.Position;

import java.awt.*;

/**
 * 一个实现了 {@link Position} 接口的自定义定位策略类。
 * <p>
 * 它的核心功能是为一个已有的基础 {@code Position} 对象（如 {@link net.coobird.thumbnailator.geometry.Positions#BOTTOM_RIGHT})
 * 添加外边距（Margins）功能。
 * <p>
 * 该类首先会委托基础 {@code Position} 对象计算出初始坐标，然后根据设定的上、下、左、右边距值
 * 对这个坐标进行微调，从而实现将水印从容器边缘向内推移的效果。
 *
 * @see Position
 * @see net.coobird.thumbnailator.geometry.Positions
 */
public class MarginPosition implements Position {

    /**
     * 被包装的原始位置对象。所有的坐标计算都将首先基于此对象的结果。
     */
    private final Position basePosition;

    /**
     * 上边距值（单位：像素）。
     */
    private final int marginTop;

    /**
     * 下边距值（单位：像素）。
     */
    private final int marginBottom;

    /**
     * 左边距值（单位：像素）。
     */
    private final int marginLeft;

    /**
     * 右边距值（单位：像素）。
     */
    private final int marginRight;

    /**
     * 构造一个新的 {@code MarginPosition} 实例。
     *
     * @param basePosition  基础位置对象，不可为 {@code null}。
     * @param marginTop     上边距值（像素）。
     * @param marginBottom  下边距值（像素）。
     * @param marginLeft    左边距值（像素）。
     * @param marginRight   右边距值（像素）。
     */
    public MarginPosition(Position basePosition, int marginTop, int marginBottom, int marginLeft, int marginRight) {
        if (basePosition == null) {
            throw new IllegalArgumentException("Base position cannot be null.");
        }
        this.basePosition = basePosition;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
    }

    /**
     * 计算并返回水印在目标图片中的精确坐标点。
     * <p>
     * 此实现首先调用 {@code basePosition} 的 {@code calculate} 方法获取一个基准点，
     * 然后根据 {@code marginLeft}/{@code marginRight} 和 {@code marginTop}/{@code marginBottom} 的值
     * 对该点进行调整，最终返回调整后的新坐标。
     *
     * @param enclosingWidth  容器（目标图片）的宽度。
     * @param enclosingHeight 容器（目标图片）的高度。
     * @param width           被放置元素（水印）的宽度。
     * @param height          被放置元素（水印）的高度。
     * @param insetLeft       容器的左侧内边距。
     * @param insetRight      容器的右侧内边距。
     * @param insetTop        容器的顶部内边距。
     * @param insetBottom     容器的底部内边距。
     * @return 一个包含最终 X 和 Y 坐标的 {@link Point} 对象。
     */
    @Override
    public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height,
                           int insetLeft, int insetRight, int insetTop, int insetBottom) {
        // 1. 先用基础位置计算出原始坐标点
        Point basePoint = basePosition.calculate(
                enclosingWidth, enclosingHeight, width, height,
                insetLeft, insetRight, insetTop, insetBottom
        );
        // 2. 在原始坐标点的基础上，应用边距进行微调
        // X 坐标：向右移动 marginLeft，向左移动 marginRight
        int newX = basePoint.x + marginLeft - marginRight;
        // Y 坐标：向下移动 marginTop, 向上移动 marginBottom
        int newY = basePoint.y + marginTop - marginBottom;
        return new Point(newX, newY);
    }
}
