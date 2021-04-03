package com.taylor.tooz.utils;

import java.io.File;
import java.util.Optional;

/**
 * *****************************************************************
 *                       hierarchy util
 * getHierarchy: 获取文件的层级
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.31 v1.0
 * *****************************************************************
 */
public class HierarchyUtil {
    public static final String fileSepartor = Optional.ofNullable(System.getProperty("file.separator")).orElse("\\");
    public static final String HIERARCHY_REPLACE_REGEX = "[-\\\\]";
    public static final String HIERARCHY_TAG = "--";
    public static final String HIERARCHY_BLANK = " ";
    public static final String HIERARCHY_TREE = "|";
    public static final String HIERARCHY_TREE_END = "\\";

    /**
     * 根据文件路径的层次，进行格式化输出，增加tag
     *
     * @param file
     * @return
     */
    private static int getHierarchy(File file) {
        int tag = 0;
        char[] filePath = file.getAbsolutePath().toCharArray();
        for (int i = 0; i < filePath.length; i++) {
            if (filePath[i] == '/' || filePath[i] == '\\') {
                tag++;
            }
        }
        return tag;
    }
}
