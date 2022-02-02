package com.taylor.tooz.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * log util
 *
 * @author taylor
 * @since 2022/2/2 10:00
 */
public class LogUtil {
    public static OutputStream log;

    private static final boolean LOG_SWITCH = true;

    private static final String LOG_NAME = ".tzfs.log";

    /* [date] [thread] [info]: log something here */
    private static final String LOG_PATTERN = "[%s]  [%s]  [%s]:  %s";

    private static final PrintWriter console = new PrintWriter(System.out);


    static {
        String homeDir = System.getProperty("user.home");
        List<File> rootfs = new ArrayList<>();
        for (File rootDir : File.listRoots()) {
            rootfs.add(rootDir);
        }

        try {
            Path logFile = Path.of(homeDir, LOG_NAME);
            log = new BufferedOutputStream(new FileOutputStream(logFile.toFile()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static void debug(Object msg) {
        log("debug", msg);
    }


    public static void info(Object msg) {
        log("info", msg);
    }


    public static void warn(Object msg) {
        log("warn", msg);
    }


    public static void error(Object msg) {
        log("error", msg);
    }


    private static void log(String level, Object msg) {
        console.println(msg);
        String fullLog = String.format(LOG_PATTERN,
                LocalDateTime.now(),
                Thread.currentThread(),
                level,
                msg);
        // write log to file
        try {
            log.write(fullLog.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            info(e.getStackTrace());
        }
    }
}
