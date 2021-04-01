package com.taylor.tooz.explorer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.taylor.tooz.utils.HierarchyUtil.*;


/**
 * *****************************************************************
 *                         file explorer
 * recurseLookUp: 获取当前目录所有文件信息
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.31 v1.0
 * *****************************************************************
 */
public class Explorer {
    public static final ConcurrentHashMap<String, Object> recordMap = new ConcurrentHashMap<>();
    private static final String RECORD_FILE = "recordFile";
    private static final String RECORD_DIR = "recordDir";
    private static final String filePath = File.listRoots()[1].getPath();
    public static OutputStream LOG_FILE;


    static {
        try {
            LOG_FILE = new FileOutputStream(filePath + "/tmp");
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }



    /**
     * 获取文件树
     *
     * @param filePath
     * @return
     */
    public void listFileTree(String filePath) {
        System.out.println("tree " + filePath + " running on " + System.getProperty("os.name"));
        String fileSepartor = Optional.ofNullable(System.getProperty("file.separator")).orElse("\\");
        listSubFiles(new File(filePath), "");
        System.out.println("\n" + recordMap.get(RECORD_DIR) + " directories, " + recordMap.get(RECORD_FILE) + " files");
    }


    private void listSubFiles(File file, String tag) {
        try {
            LOG_FILE.write((tag + file.getName() + "\n").getBytes());
        } catch (Throwable e) {
            System.out.println("error");
            return;
        }


        // 记录文件与文件夹个数
        if (!file.isDirectory()) {
            int fileRecord = (int) Optional.ofNullable(recordMap.get(RECORD_FILE)).orElse(0);
            recordMap.put(RECORD_FILE, ++fileRecord);
            return;
        }
        int dirRecord = (int) Optional.ofNullable(recordMap.get(RECORD_DIR)).orElse(0);
        recordMap.put(RECORD_DIR, ++dirRecord);

        try {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            // 格式化输出目录层次结构，把上个文件的层次tag替换为空格
            tag = tag.replaceAll(HIERARCHY_REPLACE_REGEX, HIERARCHY_BLANK)
                    + HIERARCHY_TREE + HIERARCHY_TAG;
            for (int index = 0; index < childFiles.length; index++) {
                // 最后一个文件用\表示该目录结尾，但保留主目录最后一个文件的tag
                if (index == childFiles.length - 1) {
                    String endTag = tag.substring(0, tag.lastIndexOf(HIERARCHY_TREE))
                            + HIERARCHY_TREE_END
                            + tag.substring(tag.lastIndexOf(HIERARCHY_TREE) + 1);
                    tag = endTag.contains(HIERARCHY_TREE) ? endTag : tag;
                }

                listSubFiles(childFiles[index], tag);
            }
        } catch (Throwable e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }




    /**
     * 获取当前目录所有文件信息，并存入Map
     *
     * @param filePath
     */
    public static void recurseLookUp(String filePath) {
        // 以(文件路径字符串, 文件)记录并标识文件信息
        Map<String, File> hierarchy = new HashMap<>();
        hierarchy.put(filePath, new File(filePath));
        // 如果文件是文件夹类型，则将子文件加入hierarchy
        hierarchy = getFiles(hierarchy, filePath);
        // 输入为:quit，则退出
        while (!(filePath = new Scanner(System.in).nextLine()).equals(":quit")) {
            // 输入为:read并指定文件名，则输出文件内容
            if (filePath.equals(":read")) {
                getFileDetails(hierarchy.get(new Scanner(System.in).nextLine()));
                continue;
            }
            // 继续循环子文件
            hierarchy = getFiles(hierarchy, filePath);
        }
    }


    /**
     * 将文件夹内所有的子文件加入Map
     *
     * @param hierarchy
     * @param fileName
     * @return
     */
    private static Map<String, File> getFiles(Map<String, File> hierarchy, String fileName) {
        File filePath = hierarchy.get(fileName);
        Map<String, File> newHierarchy = new HashMap<>();
        System.out.println("★-------" + filePath.getName() + "------★");

        // 如果是文件夹，则遍历获取所有子文件
        try {
            if (filePath.isDirectory()) {
                File[] childFiles = filePath.listFiles();
                for (File childFile : childFiles) {
                    // 格式化输出子文件的路径
                    System.out.println(getHierarchy(childFile) + "--" + childFile.getName());
                    newHierarchy.put(childFile.getName(), childFile);
                }
            }
        } catch (Throwable e) {
            System.out.println("deny to access! " + e);
        } finally {
            System.out.println("★-------" + "END OF LIST" + "------★");
        }

        return newHierarchy;
    }


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

    /**
     * 获取文件内容
     *
     * @param file
     */
    private static void getFileDetails(File file) {
        try {
            FileReader fr = new FileReader(file);
            char[] chars = new char[10];
            int n;
            while ((n = fr.read(chars)) != -1) {
                System.out.print(chars.toString());
            }
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
//        recurseLookUp(filePath);
//        showNIO();
//        walkDir();
//        searchFile();
        new Explorer().listFileTree(filePath);
    }


}


















