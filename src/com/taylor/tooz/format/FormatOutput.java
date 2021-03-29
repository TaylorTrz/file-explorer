package com.taylor.tooz.format;

import java.io.IOException;

/**
 * *****************************************************************
 *                       format output
 * showWelcomeScreen: 展示欢迎界面
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021/03/29 v1.0
 * *****************************************************************
 */
public class FormatOutput {
    public static final String FORMAT_ASTERISK = "******************************";
    public static final String FORMAT_PLUS = "++++++++++++++++++++++++++++++";
    public static final String FORMAT_DASH = "-------------------------------";
    public static final String LINE_END = "\n";
    public static final String FUNCTION_1 = "1.  文件系统目录";
    public static final String FUNCTION_2 = "2.  重复文件查杀器";
    public static final String FUNCTION_3 = "3.  帮助手册";
    public static final String ERROR_OUTPUT = "请选择对应的数字，回车确认选择！";

    public static void showWelcomeScreen() {
        String welcomeScreen = FORMAT_ASTERISK + LINE_END
                + FORMAT_PLUS + LINE_END
                + FUNCTION_1 + LINE_END
                + FUNCTION_2 + LINE_END
                + FUNCTION_3 + LINE_END
                + ERROR_OUTPUT + LINE_END
                + FORMAT_PLUS + LINE_END
                + FORMAT_ASTERISK;
        System.out.println(welcomeScreen);
    }


    public static void main(String[] args) {
        showWelcomeScreen();

        try {
            Runtime.getRuntime().exec("ipconfig /all");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
