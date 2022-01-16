package com.taylor.tooz.git;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

/**
 * *****************************************************************
 * Helper to log empty directories in git
 * -----------------------------------------------------------------
 * See help reference in 3-git-helper-man
 * <p>
 * -----------------------------------------------------------------
 *
 * @author taoruizhe
 * @since 2022/01/16
 * *****************************************************************
 */
public class EmptyDirHelper {
    private static final String GIT_KEEP_FILENAME = ".gitkeep";


    /**
     * start entry
     *
     * @author taoruizhe
     * @since 2022/01/16
     */
    public void start() {
        for (; ; ) {
            String in = new Scanner(System.in).nextLine();

            if (in == null || in.isEmpty()) {
                continue;
            } else if (in.equals(INPUT.HELP.getCommand())) {
                continue;
            } else if (in.equals(INPUT.EXIT.getCommand())) {
                break;
            } else {
                Path dirPath = Paths.get(in);
                try {
                    if (!Files.exists(dirPath)) {
                        System.out.printf("dirPath %s not exist!\n", dirPath);
                        continue;
                    }
                    addKeepToDir(dirPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    enum INPUT {
        HELP("help"),
        EXIT("exit");

        private String command;

        INPUT(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }


    private boolean addKeepToDir(Path dirPath) throws Exception {

        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path path, IOException ioe) throws IOException {
                if (Files.isDirectory(path)) {
                    File dir = path.toFile();
                    File[] subFiles = dir.listFiles();
                    if (subFiles == null || subFiles.length == 0) {
                        // add .gitkeep file
                        String gkAbsPath = Paths.get(path.toString(), GIT_KEEP_FILENAME)
                                .toString();
                        System.out.printf("add .gitkeep file %s \n", gkAbsPath);
                        Files.createFile(Paths.get(gkAbsPath));
                    }
                }
                return FileVisitResult.CONTINUE;
            }

        });

        return true;
    }
}
