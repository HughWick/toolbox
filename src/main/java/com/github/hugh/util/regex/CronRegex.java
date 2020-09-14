package com.github.hugh.util.regex;

import com.github.hugh.util.EmptyUtils;

import java.util.EnumMap;

import static com.github.hugh.util.regex.CronRegex.Field.*;

/**
 * quartz 定时任务corn表达式正则校验
 *
 * @author hugh
 * @since 1.1.0
 */
public class CronRegex {

    /**
     * 单位枚举
     */
    public enum Field {
        sec, min, hour, day, month, dayOfWeek, year
    }

    /**
     * 校验字符串中的cron表达式中是否为每秒钟启动、并且在30秒以下则返回true
     *
     * @param str cron表达式
     * @return boolean {@code true} 周期低于30秒
     */
    public static boolean isTooShort(String str) {
        if (str == null) {
            return true;
        }
        String[] arr = str.split(" ");
        boolean flag = true;
        int length = arr.length;
        for (int i = 1; i < length; i++) {//遍历获取 秒位后的格式
            String v = arr[i];
            if (v.equals("*") || v.equals("?")) {//分、时、月、都为星号时不处理

            } else {
                flag = false;
            }
        }
        if (flag) {//秒数定时调度
            String top = arr[0];
            if (top.contains("*")) {//每秒
                return true;
            } else if (top.contains("-")) {//周期
                //周期不处理、默认返回false
            } else if (top.contains("/")) {//从X秒开始、每X秒执行一次
                String[] sec = top.split("/");
                return Integer.valueOf(sec[1]) < 30;//每秒循环小于30秒返回true
            } else {//指定
                return Integer.valueOf(top) < 30;//每秒循环小于30秒返回true
            }
        }
        return false;
    }

    /**
     * 验证字符串是否为Cron表达式
     *
     * @param string Cron表达式
     * @return boolean {@code true} 正确的调度表达式
     */
    public static boolean isCron(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        String cronRegex = createCronRegex();
        return string.matches(cronRegex);
    }


    /**
     * 创建Cron表达式验证规则
     *
     * @return String 正则表达式
     */
    // originally copied from http://stackoverflow.com/questions/2362985/verifying-a-cron-expression-is-valid-in-java
    private static String createCronRegex() {
        EnumMap<Field, String> regexByField = new EnumMap<>(Field.class);
        regexByField.put(sec, "[0-5]?\\d");
        regexByField.put(min, "[0-5]?\\d");
        regexByField.put(hour, "[01]?\\d|2[0-3]");
        regexByField.put(day, "0?[1-9]|[12]\\d|3[01]");
        regexByField.put(month, "[1-9]|1[012]");
        regexByField.put(dayOfWeek, "[0-6]");
        regexByField.put(year, "|\\d{4}");
        // expand regex to contain different time specifiers
        for (Field field : Field.values()) {
            String number = regexByField.get(field);
            String range =
                    "(?:" + number + ")" +
                            "(?:" +
                            "(?:-|/|," + (dayOfWeek == field ? "|#" : "") + ")" +
                            "(?:" + number + ")" +
                            ")?";
            if (field == dayOfWeek) range += "(?:L)?";
            if (field == month) range += "(?:L|W)?";
            regexByField.put(field, "\\?|\\*|" + range + "(?:," + range + ")*");
        }
        // add string specifiers
        String monthValues = "JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC";
        String monthRange = "(?:" + monthValues + ")(?:(?:-)(?:" + monthValues + "))?";
        regexByField.put(month, regexByField.get(month) + "|\\?|\\*|" + monthRange + "(?:," + monthRange + ")*");
        String dayOfWeekValues = "MON|TUE|WED|THU|FRI|SAT|SUN";
        String dayOfWeekRange = "(?:" + dayOfWeekValues + ")(?:(?:-)(?:" + dayOfWeekValues + "))?";
        regexByField.put(dayOfWeek, regexByField.get(dayOfWeek) + "|\\?|\\*|" + dayOfWeekRange + "(?:," + dayOfWeekRange + ")*");
        return "^\\s*($" +
                "|#" +
                "|\\w+\\s*=" +
                "|" +
                "(" + regexByField.get(sec) + ")\\s+" +
                "(" + regexByField.get(min) + ")\\s+" +
                "(" + regexByField.get(hour) + ")\\s+" +
                "(" + regexByField.get(day) + ")\\s+" +
                "(" + regexByField.get(month) + ")\\s+" +
                "(" + regexByField.get(dayOfWeek) + ")(|\\s)+" +
                "(" + regexByField.get(year) + ")" +
                ")$";
    }
}
