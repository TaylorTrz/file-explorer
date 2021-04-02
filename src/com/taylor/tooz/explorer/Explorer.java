package com.taylor.tooz.explorer;

import com.taylor.tooz.format.FormatInput;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.taylor.tooz.utils.HierarchyUtil.*;


/**
 * *****************************************************************
 * file explorer
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
     * explorer
     */
    public void explore() {
        Map<String, File> hierarchy = new HashMap<>();
        hierarchy.put(filePath, new File(filePath));

        while (true) {
            String input = new Scanner(System.in).nextLine();
            FormatInput.Command command = FormatInput.getCommand(input);
            if (command == FormatInput.Command.COMMAND_HELP) {
                System.out.println("help");
                continue;
            }

            if (command == FormatInput.Command.COMMAND_READ) {
                System.out.println("read");
                continue;
            }

            if (command == FormatInput.Command.COMMAND_CHANGE_DIR) {
                hierarchy = changeDirectory(hierarchy, new File(""));
                continue;
            }

            if (command == FormatInput.Command.COMMAND_TREE) {
                new Explorer().listFileTree(File.listRoots()[1].getPath());
                continue;
            }

            if (command == FormatInput.Command.COMMAND_QUIT) {
                Runtime.getRuntime().exit(0);
                System.exit(0);
            }
        }
    }


    /**
     * change directory
     */
    private Map<String, File> changeDirectory(Map<String, File> hierarchy, File file) {
        listFiles(hierarchy, filePath);
        return hierarchy;
    }


    /**
     * list files
     */
    private Map<String, File> listFiles(Map<String, File> hierarchy, File directory) {
        if (!directory.isDirectory()) {
            return hierarchy;
        }
        hierarchy = listFiles(hierarchy, filePath);
        for (Map.Entry<String, File> entry : hierarchy.entrySet()) {
            System.out.println(entry.getValue().getName());
        }
        return hierarchy;
    }


    /**
     * read file
     */
    private void readFile(File file) {
        getFileDetails(file);
    }


    /**
     * file tree
     *
     * @param filePath
     * @return
     */
    private void listFileTree(String filePath) {
        System.out.println("tree " + filePath + " running on " + System.getProperty("os.name"));
        String fileSepartor = Optional.ofNullable(System.getProperty("file.separator")).orElse("\\");
        recurseFiles(new File(filePath), "");
        System.out.println("\n" + recordMap.get(RECORD_DIR) + " directories, " + recordMap.get(RECORD_FILE) + " files");
    }


    private void recurseFiles(File file, String tag) {
        try {
            LOG_FILE.write((tag + file.getName() + "\n").getBytes());
        } catch (Throwable e) {
            System.out.println("error to open log file");
            return;
        }

        // log files/directories number
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
            // format and pretty output hierarchy
            tag = tag.replaceAll(HIERARCHY_REPLACE_REGEX, HIERARCHY_BLANK)
                    + HIERARCHY_TREE + HIERARCHY_TAG;
            for (int index = 0; index < childFiles.length; index++) {
                // last file in dir will tag with "\", but keep tag "|" of last file in main directory
                if (index == childFiles.length - 1) {
                    String endTag = tag.substring(0, tag.lastIndexOf(HIERARCHY_TREE))
                            + HIERARCHY_TREE_END
                            + tag.substring(tag.lastIndexOf(HIERARCHY_TREE) + 1);
                    tag = endTag.contains(HIERARCHY_TREE) ? endTag : tag;
                }

                recurseFiles(childFiles[index], tag);
            }
        } catch (Throwable e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }


    /**
     * 将文件夹内所有的子文件加入Map
     *
     * @param hierarchy
     * @param fileName
     * @return
     */
    private static Map<String, File> listFiles(Map<String, File> hierarchy, String fileName) {
        File file = hierarchy.get(fileName);
        Map<String, File> newHierarchy = new HashMap<>();

        if (file.isDirectory()) {
            try {
                File[] childFiles = file.listFiles();
                for (File childFile : childFiles) {
                    newHierarchy.put(childFile.getName(), childFile);
                }
            } catch (Throwable e) {
                System.out.println("deny to access file");
            }
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


















