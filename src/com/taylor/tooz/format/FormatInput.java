package com.taylor.tooz.format;

import com.taylor.tooz.explorer.Explorer;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

import static com.taylor.tooz.explorer.Explorer.RECORD_CURRENT;

/**
 * *****************************************************************
 * format input
 * getInput: 获取用户输入并转换为标准输入格式
 * -----------------------------------------------------------------
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.31 v1.0
 * *****************************************************************
 */
public class FormatInput {
    public static final String ERROR_INPUT = "输入错误！请使用help命令查看帮助文档！\n";
    public static final String BLANK_INPUT = "[ ]";

    public enum Command {
        READ("cat", "print"),
        EDIT("vi", "vim"),
        REMOVE("rm", "del"),
        CREATE_FILE("touch", "create"),
        CREATE_DIR("md", "mkdir"),
        FIND("find", "find"),
        TREE("tree", "tree"),
        CHANGE_DIR("cd", "cd"),
        SHOW_DIR("pwd", "pwd"),
        SHOW_FILE("ls", "dir"),
        CLEAR("cls", "clear"),
        HELP("man", "help"),
        QUIT("exit", "quit"),
        ERROR("error", "error");

        private String abbreviation;
        private String full;

        Command(String abbreviation, String full) {
            this.abbreviation = abbreviation;
            this.full = full;
        }

        public String getAbbreviation() {
            return this.abbreviation;
        }

        public String getFull() {
            return this.full;
        }
    }


    /**
     * simplify user input
     *
     * @return
     */
    public static Command getCommand(String input) {
        for (Command cmd : Command.values()) {
            if (input.split(BLANK_INPUT)[0].equals(cmd.getAbbreviation())
                    || input.split(BLANK_INPUT)[0].equals(cmd.getFull()))
                return cmd;
        }
        return Command.ERROR;
    }


    public static String input() {
        File currentFile = Optional.ofNullable((File) Explorer.hierarchy.get(RECORD_CURRENT)).orElse(new File("/"));
        String currentFilePath = currentFile.getAbsolutePath();
        System.out.printf("taylor@file-explorer:%s$ ", currentFilePath);
        return new Scanner(System.in).nextLine();
    }

    public static void main(String[] args) {
        while (true) {
            input();
        }
    }
}
