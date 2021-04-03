package com.taylor.tooz.explorer;

import com.taylor.tooz.format.FormatInput;
import com.taylor.tooz.format.FormatOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    public static final Map<String, Object> hierarchy = new ConcurrentHashMap<>();
    private static final String RECORD_FILE = "recordFile";
    private static final String RECORD_DIR = "recordDir";
    public static final String RECORD_CURRENT = "recordCurrent";
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
    public void start() {
        hierarchy.put(RECORD_CURRENT, new File(filePath));

        while (true) {
            String input = FormatInput.input();
            FormatInput.Command command = FormatInput.getCommand(input);
            if (command == FormatInput.Command.HELP) {
                System.out.println("help");
                continue;
            }

            if (command == FormatInput.Command.READ) {
                System.out.println("read");
                continue;
            }

            if (command == FormatInput.Command.SHOW_FILE) {
                listFiles(input);
                continue;
            }

            if (command == FormatInput.Command.SHOW_DIR) {
                System.out.println(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath());
                continue;
            }

            if (command == FormatInput.Command.CHANGE_DIR) {
                changeDirectory(input);
                continue;
            }

            if (command == FormatInput.Command.CLEAR) {
                try {
                    Runtime.getRuntime().exec("cmd /k start cls");
                } catch (Throwable e) {
                    System.out.println("failed to execute command cls");
                }
                continue;
            }

            if (command == FormatInput.Command.TREE) {
                listFileTree(File.listRoots()[1].getPath());
                continue;
            }

            if (command == FormatInput.Command.QUIT) {
                System.out.println("exit file-explorer!");
                break;
            }

            System.out.println(FormatInput.ERROR_INPUT);
        }
    }


    /**
     * change directory
     */
    private void changeDirectory(String input) {
        String dirPath = "";
        // parse input
        String[] inputArray = input.split(" ");
        if (inputArray.length > 2) {
            System.out.println(FormatInput.ERROR_INPUT);
            return;
        }
        // return to home directory
        if (inputArray.length == 1) {
            dirPath = File.listRoots()[0].getAbsolutePath();
        }
        if (inputArray.length == 2) {
            dirPath = inputArray[1];
            // relative path
            if (!inputArray[1].contains(fileSepartor)) {
                dirPath = ((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + fileSepartor + inputArray[1];
            }
            // return to parent directory
            if ("..".equals(inputArray[1])) {
                File currentDir = (File) hierarchy.get(RECORD_CURRENT);
                dirPath = currentDir.getParentFile().getAbsolutePath();
            }
        }

        File directory = new File(dirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.printf("directory %s not exist!\n", dirPath);
            return;
        }

        try {
            hierarchy.clear();
            hierarchy.put(RECORD_CURRENT, directory);
            File[] childFiles = directory.listFiles();
            for (File childFile : childFiles) {
                hierarchy.put(childFile.getName(), childFile);
            }
        } catch (Throwable e) {
            System.out.println("deny to access file");
        }
    }


    /**
     * list files
     */
    private void listFiles(String input) {
        File currentDir = (File) hierarchy.get(RECORD_CURRENT);

        String[] inputArray = input.split(" ");
        if (inputArray.length > 2) {
            System.out.println(FormatInput.ERROR_INPUT);
            return;
        }
        if (inputArray.length == 2) {
            String dirName = inputArray[1];
            currentDir = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + "/" + dirName);
            if (!currentDir.exists() || !currentDir.isDirectory()) {
                System.out.println(String.format("directory %s not exist!", dirName));
            }
        }

        File[] files;
        if ((files = currentDir.listFiles()) == null)
            return;
        for (File file : files) {
            System.out.println(file.getName());
        }
    }


    /**
     * read file
     */
    private void readFile(File file) {
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


    /**
     * edit file
     */
    private void editFile(File file) {
        if (!file.exists() || file.isDirectory()) {
            FormatOutput.log(String.format("file %s not exist", file.getName()));
            return;
        }

        if (!file.canWrite()) {
            FormatOutput.log(String.format("file %s deny to write", file.getName()));
            return;
        }
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            while (true) {
                String input = new Scanner(System.in).nextLine();
                if (":q".equals(input)) {
                    // exit without save
                    outputStream.close();
                    return;
                }
                if (":wq".equals(input)) {
                    // save and exit
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
                outputStream.write((input + "\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (Throwable e) {
            FormatOutput.log(e.getMessage());
        }

    }


    /**
     * find file location or search content in which file
     *
     * @param input
     */
    private String find(String input) {
        StringBuilder location = new StringBuilder();
        File[] rootFiles = File.listRoots();

        String[] inputArray = input.split(" ");
        if (inputArray.length == 2) {
            System.out.println("May take long time to find file in root filesystem, consider specify directory!");
            for (File rootFile : rootFiles) {
                //
            }
        }
        if (inputArray.length == 4) {
            if ("-name".equals(inputArray[2])) {
                //
            }
            if ("-content".equals(inputArray[2])) {
                //
            }
        }

        return location.toString();
    }


    /**
     * create new file
     *
     * @param hierarchy
     * @param input
     */
    public void createFile(Map<String, File> hierarchy, String input) {
        String fileName = input.split(" ")[1];
        String absolutePath = hierarchy.get(RECORD_CURRENT).getAbsolutePath() + "/" + fileName;
        File file = new File(absolutePath);
        if (file.exists()) {
            FormatOutput.log("current file already exist!");
            return;
        }

        try {
            file.createNewFile();
        } catch (Throwable e) {
            FormatOutput.log(e.getMessage());
        }
    }


    /**
     * create new directory
     *
     * @param hierarchy
     * @param input
     */
    public void createDir(Map<String, File> hierarchy, String input) {
        String directoryName = input.split(" ")[1];
        File dir = new File(hierarchy.get(RECORD_CURRENT).getAbsolutePath() + "/" + directoryName);
        if (dir.exists()) {
            FormatOutput.log("current directory already exist!");
            return;
        }
        try {
            dir.mkdir();
        } catch (Throwable e) {
            FormatOutput.log(e.getMessage());
        }

    }


    /**
     * file tree
     *
     * @param filePath
     * @return
     */
    private void listFileTree(String filePath) {
        System.out.println("tree " + filePath + " running on " + System.getProperty("os.name"));
        recurseFiles(new File(filePath), "");
        System.out.println("\n" + hierarchy.get(RECORD_DIR) + " directories, " + hierarchy.get(RECORD_FILE) + " files");
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
            int fileRecord = (int) Optional.ofNullable(hierarchy.get(RECORD_FILE)).orElse(0);
            hierarchy.put(RECORD_FILE, ++fileRecord);
            return;
        }
        int dirRecord = (int) Optional.ofNullable(hierarchy.get(RECORD_DIR)).orElse(0);
        hierarchy.put(RECORD_DIR, ++dirRecord);

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


    public static void main(String[] args) throws IOException {
//        recurseLookUp(filePath);
//        showNIO();
//        walkDir();
//        searchFile();
        // new Explorer().editFile(new File(filePath + "\\" + "tmp"));
        new Explorer().start();
    }


}


















