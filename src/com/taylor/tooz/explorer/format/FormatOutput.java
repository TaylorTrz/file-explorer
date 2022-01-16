package com.taylor.tooz.explorer.format;

import com.taylor.tooz.Main;
import com.taylor.tooz.explorer.Explorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import static com.taylor.tooz.explorer.Explorer.RECORD_CURRENT;

/**
 * *****************************************************************
 * format output
 * showWelcomeScreen: 展示欢迎界面
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.29 v1.0
 * *****************************************************************
 */
public class FormatOutput {
    public static final String FORMAT_ASTERISK = "**************************************************";
    public static final String FORMAT_DASH = "--------------------------------------------------";
    public static final String LINE_START = "\t\t|";
    public static final String LINE_START_1 = "\t\t/";
    public static final String LINE_START_2 = "\t\t\\";
    public static final String LINE_END = "|\n";
    public static final String LINE_END_1 = "\\\n";
    public static final String LINE_END_2 = "/\n";
    public static final String LEFT_SLASH = "/";
    public static final String RIGHT_SLASH = "\\";
    public static final String VERSION = String.format("%10s%-40s", " ", "file-explorer v1.0 by taoruizhe");
    public static final String FUNCTION_1 = String.format("%15s%-35s", " ", "1.  file-explorer");
    public static final String FUNCTION_2 = String.format("%15s%-35s", " ", "2.  double-killer");
    public static final String FUNCTION_3 = String.format("%15s%-35s", " ", "3.  help manual");
    public static final String FUNCTION_4 = String.format("%15s%-35s", " ", "4.  exit");
    public static final String ERROR_OUTPUT = "请按照功能选择对应的数字，回车确认选择！";


    /**
     * welcome screen
     */
    public static String welcomeScreen = "";
    public static String helpManual = "";
    public static String HELP_MANUAL_FILE = "resources/help-manual";

    static {
        welcomeScreen = LINE_START_1 + FORMAT_DASH + LINE_END_1
                + LINE_START + VERSION + LINE_END
                + LINE_START + FORMAT_DASH + LINE_END
                + LINE_START + FUNCTION_1 + LINE_END
                + LINE_START + FUNCTION_2 + LINE_END
                + LINE_START + FUNCTION_3 + LINE_END
                + LINE_START + FUNCTION_4 + LINE_END
                + LINE_START_2 + FORMAT_DASH + LINE_END_2
                + ERROR_OUTPUT;

        StringBuilder contentBuilder = new StringBuilder(helpManual);
        try {
            // Files类不可用于加载jar包中的静态资源，只在调试阶段会正常加载到资源文件
            Files.readAllLines(Paths.get(HELP_MANUAL_FILE))
                    .forEach((output) -> contentBuilder.append(output).append("\n"));
        } catch (Throwable e) {
        }
        // 当打成Jar后，需要使用classloader加载资源文件
        if (contentBuilder.length() == 0) {
            helpManual = loadFileContent(HELP_MANUAL_FILE);
        } else {
            helpManual = contentBuilder.toString();
        }
        if (helpManual.length() == 0) {
            System.out.println("failed to load help manual file! ");
        }
    }


    public static String loadFileContent(String file) {
        StringBuffer content = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(Main.class
                            .getClassLoader()
                            .getResourceAsStream(file))));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return content.toString();
    }


    /**
     * initial screen
     */
    public static void initialScreen() {
        StringBuilder mode = new StringBuilder("\b\b");
        StringBuilder progress = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            progress.append("-");
            mode.append("\b");
        }
        System.out.print("[" + progress.toString() + "]");
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(i);
                progress.replace(i, i + 1, ">");
                System.out.print(mode.toString() + "[" + progress.toString() + "]");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        System.out.println("\n" + "START SUCCESS!");
    }


    /**
     * format output
     *
     * @param output
     * @return
     */
    public static void log(String output) {
        File currentFile = Optional.ofNullable((File) Explorer.hierarchy.get(RECORD_CURRENT)).orElse(new File("/"));
        String currentFilePath = currentFile.getAbsolutePath();
        System.out.printf("taylor@file-explorer:%s$ %s\n", currentFilePath, output);
    }


}
