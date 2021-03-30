package com.taylor.tooz.format;

import java.util.Scanner;


public class FormatInput {
    // public static String INPUT = new Scanner(System.in).nextLine();

    public enum Command {
        COMMAND_QUIT("q", "quit"),
        COMMAND_READ("r", "read");

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
    public String getInput() {
        String input = new Scanner(System.in).nextLine();

        for (Command cmd : Command.values()) {
            if (input.equals(cmd.getAbbreviation()) || input.equals(cmd.getFull()))
                return cmd.getFull();
        }
        throw new RuntimeException(FormatOutput.ERROR_OUTPUT);
    }


    public static void main(String[] args) {
        while(true) {
            String input = new FormatInput().getInput();
            System.out.println(input);
        }
    }
}
