package com.taylor.tooz.format;

import java.io.IOException;

/**
 * *****************************************************************
 *                       format output
 * showWelcomeScreen: 展示欢迎界面
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.29 v1.0
 * *****************************************************************
 */
public class FormatOutput {
    public static final String FORMAT_ASTERISK = "****************************************";
    public static final String FORMAT_PLUS = "++++++++++++++++++++++++++++++++++++++++";
    public static final String FORMAT_DASH = "-----------------------------------------";
    public static final String LINE_END = "\n";
    public static final String FUNCTION_1 = "1.  文件系统目录";
    public static final String FUNCTION_2 = "2.  重复文件查杀器";
    public static final String FUNCTION_3 = "3.  帮助手册";
    public static final String ERROR_OUTPUT = "请选择对应的数字，回车确认选择！";


    /**
     * 欢迎界面
     */
    public static void showWelcomeScreen() {
        String welcomeScreen = FORMAT_PLUS + LINE_END
                + FORMAT_DASH + LINE_END
                + FUNCTION_1 + LINE_END
                + FUNCTION_2 + LINE_END
                + FUNCTION_3 + LINE_END
                + ERROR_OUTPUT + LINE_END
                + FORMAT_DASH + LINE_END
                + FORMAT_PLUS;
        System.out.println(welcomeScreen);
    }


    /**
     * 启动界面
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


    public static void main(String[] args) {
        initialScreen();
        showWelcomeScreen();

        try {
            Runtime.getRuntime().exec("ipconfig /all");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
