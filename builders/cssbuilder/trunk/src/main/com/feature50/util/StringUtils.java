package com.feature50.util;

public class StringUtils {
    public static boolean isNullOrEmpty(String s) {
        return (s == null) || "".equals(s);
    }

    public static boolean notNullOrEmpty(String s) {
        return !isNullOrEmpty(s);
    }
}
