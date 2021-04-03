package com.taylor.tooz.format;

import com.taylor.tooz.explorer.DoubleKiller;
import com.taylor.tooz.explorer.Explorer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

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
    public static String welcomeScreen;

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


    public static void main(String[] args) {
    }

}
