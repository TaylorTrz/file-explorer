package com.taylor.tooz.explorer;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * *****************************************************************
 *                       double killer
 * 重复文件删除工具
 * 1. 显示待删除文件的位置与超链接
 * 2. 显示待删除文件的总个数与总占用空间
 * 3. 注意内存消耗
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2020.04.09 v1.0
 * *****************************************************************
 */
public class DoubleKiller {
    private static final File ROOT_FILE = new File(File.listRoots()[0].toString());

    private static final File LOG_FILE = new File("/tmp/Duplicated_Files");

    private Map<String, List<File>> duplicateFiles = new HashMap<>();




    /**
     * ==============================================================
     * 主要逻辑：
     * /** ==============================================================
     * <p>
     * <p>
     * /**
     * 1. 以空间换时间：大内存直接遍历
     */
    public void searchDuplicated(File parent) throws Exception {
        if (parent == null || parent.listFiles() == null) {
            return;
        }
        // 只对文件夹进行操作
        File[] children = parent.listFiles();
        if (children == null) {
            throw new NullPointerException();
        }
        for (File child : children) {
            // 如果是目录，则继续搜索
            if (child.isDirectory()) {
                searchDuplicated(child);
            }
            // 判断是否有重复，再存入Map
            if (duplicateFiles.get(child.getName()) != null) {
                duplicateFiles.get(child.getName()).add(child);
            } else {
                List<File> list = new ArrayList<File>();
                list.add(child);
                duplicateFiles.put(child.getName(), list);
            }
        }
    }

    public void showDuplicated(File root) throws Exception {
        searchDuplicated(root);
        for (Map.Entry<String, List<File>> entry : duplicateFiles.entrySet()) {
            // 如果List中是否长度>1, 则输出重复文件路径
            if (entry.getValue().size() > 1) {
                for (File file : entry.getValue()) {
                    System.out.println(file.getAbsolutePath());
                }
                System.out.println("\n" + "\n");
            }
        }
    }


    /**
     * 2. 以时间换空间：分治法对文件切分
     * ------------------------------------
     */


    public static void main(String[] args) {
        try {
            DoubleKiller killer = new DoubleKiller();
            long begin = System.currentTimeMillis();
            System.out.println(LocalDateTime.now().toString());
            killer.showDuplicated(ROOT_FILE);
            long end = System.currentTimeMillis();
            System.out.println("the elapsed time: " + (end - begin) / 1000 + " seconds");

            if (LOG_FILE.mkdir()) {
                System.out.println("Create log file......");
            } else {
                System.out.println("Log file already exists, continue......");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
