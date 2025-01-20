package com.github.hugh.util.lang;

/**
 * ArrayUtils类提供了对数组进行操作的实用方法
 * @since 2.7.12
 */
public class ArrayUtils {

    private ArrayUtils(){}

    /**
     * 返回一个给定byte数组的反转版本。如果输入数组为空，则返回空数组。
     *
     * @param arr 要反转的byte数组
     * @return 反转后的新byte数组
     */
    public static byte[] reverse(byte[] arr) {
        if (arr == null) {
            return new byte[0]; // 如果输入数组为null，返回null
        }
        byte[] reversedArr = new byte[arr.length];
        for (int start = 0, end = arr.length - 1; start <= end; start++, end--) {
            reversedArr[start] = arr[end];
            reversedArr[end] = arr[start];
        }
        return reversedArr;
    }
}
