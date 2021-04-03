package com.taylor.tooz;

import com.taylor.tooz.explorer.DoubleKiller;
import com.taylor.tooz.explorer.Explorer;
import java.util.Scanner;

import static com.taylor.tooz.format.FormatOutput.welcomeScreen;

public class Main {

    public static void main(String[] args) {
        System.out.println(welcomeScreen);

        while (true) {
            String input = new Scanner(System.in).nextLine();
            if ("1".equals(input)) {
                System.out.println("欢迎使用文件目录管理器v1.0");
                new Explorer().start();
                System.out.println(welcomeScreen);
                continue;
            }
            if ("2".equals(input)) {
                System.out.println("欢迎使用重复文件查杀器v1.0");
                DoubleKiller.main(new String[]{""});
                System.out.println(welcomeScreen);
                continue;
            }
            if ("3".equals(input)) {
                System.out.println("帮助手册");
                continue;
            }
            if ("4".equals(input)) {
                System.out.println("感谢使用，下次再见！");
                Runtime.getRuntime().exit(0);
            }
            System.out.println(welcomeScreen);
        }
    }
}
