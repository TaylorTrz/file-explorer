package com.taylor.tooz.format;

import com.taylor.tooz.explorer.Explorer;

import java.io.File;
import java.util.Scanner;

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

    public enum Command {
        COMMAND_QUIT("q", "quit"),
        COMMAND_READ("r", "read"),
        COMMAND_TREE("tree", "tree"),
        COMMAND_HELP("h", "help");

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
    public Command getInput() {
        String input = new Scanner(System.in).nextLine();

        for (Command cmd : Command.values()) {
            if (input.equals(cmd.getAbbreviation()) || input.equals(cmd.getFull()))
                return cmd;
        }
        System.out.println(ERROR_INPUT);
        return getInput();
    }


    public static void main(String[] args) {



    }
}
