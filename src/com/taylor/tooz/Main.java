package com.taylor.tooz;

import com.taylor.tooz.explorer.Explorer;
import com.taylor.tooz.format.FormatInput;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        while (true) {
            FormatInput.Command input = new FormatInput().getInput();
            if (input == FormatInput.Command.COMMAND_HELP) {
                System.out.println("help");
            }

            if (input == FormatInput.Command.COMMAND_READ) {
                System.out.println("read");
            }

            if (input == FormatInput.Command.COMMAND_QUIT) {
                Runtime.getRuntime().exit(0);
                System.exit(0);
            }

            if (input == FormatInput.Command.COMMAND_TREE) {
                new Explorer().listFileTree(File.listRoots()[1].getPath());
            }
        }
    }
}
