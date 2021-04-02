package com.taylor.tooz.format;

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
        COMMAND_QUIT("exit", "quit"),
        COMMAND_READ("cat", "read"),
        COMMAND_TREE("tree", "tree"),
        COMMAND_FIND("find", "find"),
        COMMAND_REMOVE("rm", "remove"),
        COMMAND_CHANGE_DIR("cd", "change"),
        COMMAND_SHOW_DIR("ls", "dir"),
        COMMAND_HELP("man", "help");

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
        return Command.COMMAND_HELP;
    }


    public static void main(String[] args) {



    }
}
