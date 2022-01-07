package com.taylor.tooz.explorer;

import com.taylor.tooz.format.FormatInput;
import com.taylor.tooz.format.FormatOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private static final String filePath = File.listRoots()[0].getPath();

    public static OutputStream LOG_FILE;

    static {
        // put C:\ or / as default root dir
        hierarchy.put(RECORD_CURRENT, new File(filePath));

        // windows has multi root dir, differ from linux / root dir
        int index = 0;
        for (File rootDir : File.listRoots()) {
            hierarchy.put(String.valueOf(index), rootDir);
            index++;
        }

        // open log file, windows for D:\tmp, linux for /tmp
        try {
            LOG_FILE = new FileOutputStream(File.listRoots()[1].getPath() + "/tmp");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * explorer
     */
    public void start() {
        while (true) {
            String input = FormatInput.input();
            if (input.isEmpty()) {
               continue;
            }
            FormatInput.Command command = FormatInput.getCommand(input);

            if (command == FormatInput.Command.READ) {
                readFile(input);
                continue;
            }

            if (command == FormatInput.Command.EDIT) {
                editFile(input);
                continue;
            }

            if (command == FormatInput.Command.REMOVE) {
                remove(input);
                continue;
            }

            if (command == FormatInput.Command.CREATE_FILE) {
                createFile(input);
                continue;
            }

            if (command == FormatInput.Command.CREATE_DIR) {
                createDir(input);
                continue;
            }

            if (command == FormatInput.Command.FIND) {
                find(input);
                continue;
            }

            if (command == FormatInput.Command.TREE) {
                listFileTree(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath());
                continue;
            }

            if (command == FormatInput.Command.CHANGE_DIR) {
                changeDirectory(input);
                continue;
            }

            if (command == FormatInput.Command.SHOW_DIR) {
                System.out.println(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath());
                continue;
            }

            if (command == FormatInput.Command.SHOW_FILE) {
                listFiles(input);
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

            if (command == FormatInput.Command.HELP) {
                System.out.println(FormatOutput.helpManual);
                continue;
            }

            if (command == FormatInput.Command.QUIT) {
                try {
                    System.out.println("exit file-explorer!");
                    LOG_FILE.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

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
        // show current directory
        if (inputArray.length == 1) {
            // dirPath = File.listRoots()[0].getAbsolutePath();

        }
        if (inputArray.length == 2) {
            dirPath = inputArray[1];
            // return to parent directory
            if ("..".equals(inputArray[1])) {
                File currentDir = (File) hierarchy.get(RECORD_CURRENT);
                dirPath = Optional.ofNullable(currentDir.getParentFile()).orElse(currentDir).getAbsolutePath();
            }
            // absolute path
            else if (inputArray[1].contains(fileSepartor)) {
                dirPath = inputArray[1];
            }
            // relative path
            else if (!inputArray[1].contains(fileSepartor)) {
                dirPath = ((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + fileSepartor + inputArray[1];
                // path based on index
                if (inputArray[1].charAt(0) >= '0' && inputArray[1].charAt(0) <= '9') {
                    dirPath = ((File) hierarchy.get(inputArray[1])).getAbsolutePath();
                }
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
            int index = 0;
            for (File childFile : childFiles) {
                hierarchy.put(String.valueOf(index++), childFile);
            }
        } catch (Throwable e) {
            System.out.println("deny to access file");
        }
    }


    /**
     * list files
     */
    private void listFiles(String input) {
        String[] inputArray = input.split(FormatInput.BLANK_INPUT);
        if (inputArray.length > 2) {
            System.out.println(FormatInput.ERROR_INPUT);
            return;
        }
        if (inputArray.length == 2) {
            String dirName = inputArray[1];
            File currentDir = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + "/" + dirName);
            if (!currentDir.exists() || !currentDir.isDirectory()) {
                System.out.println(String.format("directory %s not exist!", dirName));
            }
        }

        System.out.println(String.format("%-5s\t%-10s\t%-20s\t%-20s\t", "index", "size", "lastModifiedTime", "name"));
        for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
            if (entry.getKey().charAt(0) >= '0' && entry.getKey().charAt(0) <= '9') {
                File file = (File) entry.getValue();
                String name = ("".equals(file.getName()) ? file.getPath() : file.getName());
                try {
                    long size = Files.size(Paths.get(file.getAbsolutePath()));
                    if (file.isDirectory()) {
                        size = Files.size(Paths.get(file.getAbsolutePath()));
                    }

                    String index = entry.getKey();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    long millis = Files.getLastModifiedTime(Paths.get(file.getAbsolutePath())).toMillis();
                    LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(new Date(millis).toInstant(), ZoneId.of("Asia/Shanghai"));
                    System.out.println(String.format("%-5s\t%-10s\t%-20s\t%-20s\t",
                            index, size, lastModifiedTime.format(formatter), name));
                } catch (Throwable e) {
                    System.out.println(String.format("failed to access file/dir %s", name));
                }

            }
        }
    }


    /**
     * read file
     */
    private void readFile(String input) {
        File file = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath()
                + fileSepartor
                + input.split(FormatInput.BLANK_INPUT)[1]);
        try {
            InputStream stream = new FileInputStream(file);
            // @TODO byte array has length 100 will cause long blank
            byte[] bytes = new byte[100];
            int n;
            while ((n = stream.read(bytes)) != -1) {
                System.out.print(new String(bytes));
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * edit file
     */
    private void editFile(String input) {
        File file = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath()
                + fileSepartor
                + input.split(FormatInput.BLANK_INPUT)[1]);

        if (!file.exists() || file.isDirectory()) {
            System.out.println(String.format("file %s not exist", file.getName()));
            return;
        }
        if (!file.canWrite()) {
            System.out.println(String.format("file %s deny to write", file.getName()));
            return;
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            while (true) {
                input = new Scanner(System.in).nextLine();
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
    private void find(String input) {
        StringBuilder location = new StringBuilder();
        long startTime = System.currentTimeMillis();

        String[] inputArray = input.split(" ");
        if (inputArray.length == 2) {
            System.out.println("May take very long time to find in root filesystem, consider specify directory!");
            File[] rootFiles = File.listRoots();
            for (File rootFile : rootFiles) {
                location = searchFile(rootFile, inputArray[1], location);
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

        System.out.println(location.toString());
        System.out.printf("elapsed %d seconds!\n", (System.currentTimeMillis() - startTime) / 1000);
    }


    /**
     * search file/dir based on name
     *
     * @param file
     * @return
     */
    public StringBuilder searchFile(File file, String name, StringBuilder location) {
        if (name.equals(file.getName())) {
            location.append(file.getAbsolutePath()).append("\n");
        }

        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles == null) {
                return location;
            }
            for (File subFile : subFiles) {
                location = searchFile(subFile, name, location);
            }
        }

        return location;
    }


    /**
     * remove file/dir
     */
    public void remove(String input) {
        String fileName = input.split(FormatInput.BLANK_INPUT)[1];
        File file = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + fileSepartor + fileName);

        if (!file.exists()) {
            System.out.println(String.format("file/dir %s not exist!", file.getAbsolutePath()));
            return;
        }

        if (file.isDirectory()) {
            String result = (file.delete() ? "success" : "failed");
            System.out.println(String.format("directory %s delete %s", file.getAbsolutePath(), result));
        }

        if (file.isFile()) {
            String result = (file.delete() ? "success" : "failed");
            System.out.println(String.format("file %s delete %s", file.getAbsolutePath(), result));
        }
    }


    /**
     * create new file
     *
     * @param input
     */
    public void createFile(String input) {
        String fileName = input.split(FormatInput.BLANK_INPUT)[1];
        String absolutePath = ((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + fileSepartor + fileName;
        File file = new File(absolutePath);
        if (file.exists()) {
            System.out.println(String.format("current file %s already exist!", absolutePath));
            return;
        }

        try {
            file.createNewFile();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * create new directory
     *
     * @param input
     */
    public void createDir(String input) {
        String directoryName = input.split(FormatInput.BLANK_INPUT)[1];
        File dir = new File(((File) hierarchy.get(RECORD_CURRENT)).getAbsolutePath() + "/" + directoryName);
        if (dir.exists()) {
            System.out.println(String.format("current directory %s already exist!", dir.getAbsolutePath()));
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


}


















