package com.taylor.tooz.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final PrintStream console = System.out;


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


    public static void debug(Object... args) {
        log("debug", args);
    }


    public static void info(Object... args) {
        log("info", args);
    }


    public static void warn(Object... args) {
        log("warn", args);
    }


    public static void error(Object... args) {
        log("error", args);
    }


    private static void log(String level, Object... args) {
        StringBuilder msg = new StringBuilder();
        msg.append(args[0] == null ? "null" : (String) args[0]);
        // print stack trace
        if (args.length == 2 && args[1] instanceof Throwable) {
            Exception e = (Exception) args[1];
            msg.append("\n");
            for (StackTraceElement traceElement: e.getStackTrace()) {
                msg.append("\tat ").append(traceElement.toString()).append("\n");
            }
        }
        // write msg to console
        console.println(msg);
        // write log to file
        if (!LOG_SWITCH) {
            return;
        }
        String fullLog = String.format(LOG_PATTERN,
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(LocalDateTime.now()),
                Thread.currentThread().getName(), level, msg);
        try {
            log.write(fullLog.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            info(e.getMessage(), e);
        }

    }
}
