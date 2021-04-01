package com.taylor.tooz.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * java的NIO包测试
 *
 * @author taoruizhe100@163.com
 * @version 2021.03.31 v1.0
 */
public class NioUtil {
    public static void showNIO() throws IOException {
        // 当前使用的系统
        System.out.println(System.getProperty("os.name"));
        // java.nio.Paths与Path的使用
        Path path = Paths.get("tmp", "new.txt");
        // Path与URI转换
        URI uri = path.toUri();
        // 路径转换为文件，按理来说路径就是有意义的字符串，而不是文件的概念
        File file = path.toFile();
        System.out.println(path.toAbsolutePath().getRoot());
        System.out.println(uri);
        System.out.println(file.getAbsolutePath());

        // 两种方式都可，对Path类中的每一个path进行拆分，注意是不包括根目录的
        Path absPath = path.toAbsolutePath();
        for (Path p : absPath) {
            System.out.println(p.toAbsolutePath());
        }
        for (int i = 0; i < absPath.getNameCount(); i++) {
            System.out.println(absPath.getName(i).toAbsolutePath());
        }
        // 以文件后缀名进行判断Path，结果是失败的
        System.out.println("文件后缀判断结果：" + absPath.endsWith(".txt"));
        // 以根目录来归类Path，结果正常
        System.out.println("根目录判断结果：" + absPath.startsWith(absPath.getRoot()));

        // java.nio.file.Files类
        // 写入1000行数组
        UUID uuid = UUID.randomUUID();
        Random random = new Random(100);
        byte[] bytes = new byte[1000];
        random.nextBytes(bytes);
        Files.write(path, bytes);
        System.out.println("当前文件行数：" + Files.size(path));
        // 对字节数据进行读取
        byte[] values = Files.readAllBytes(path);

        System.out.println("当前文件行数：" + Files.size(path));
        // 直接对“小文件”进行读写
//        List<String> lines = Files.readAllLines(path);
//        lines.stream().filter(line -> line.startsWith("E")).map(line -> line + "直接读写").forEach(System.out::println);

        // java.nio.file.Files 包括了所有nio中对于文件操作的功能
        Files.exists(path);
        Files.isDirectory(path);
        Files.isHidden(path);
        Files.size(path);
        System.out.println(Files.getFileStore(path).name());
        Files.getLastModifiedTime(path);
        System.out.println(Files.probeContentType(path));
        // 链接文件
        if (Files.isSymbolicLink(path)) {
            Files.isSymbolicLink(path);
        }
    }


    public static void showFileSystem() {
        System.out.println(System.getProperty("os.name"));
        FileSystem fs = FileSystems.getDefault();
        for (FileStore store : fs.getFileStores()) {
            System.out.println(store.name());
        }
        for (Path path : fs.getRootDirectories()) {
            System.out.println(path.toUri());
        }
        fs.isOpen();
    }


    public static void walkDir() throws IOException {
        Path path = Paths.get("D:\\ideaProject\\daily_improvement\\FilePractise\\src");
        System.out.println(path.toUri());
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                Files.readAllLines(path).forEach(System.out::println);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path path, IOException exception) throws IOException {
                System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-" + Files.isDirectory(path) + "+-+-+-+-+-+-+-+-+-+-+-+-");
                return FileVisitResult.CONTINUE;
            }
        });
    }


    public static void searchFile() throws IOException {
        Path path = Paths.get("src");
        System.out.println("--------->Search all java files in " + path.toUri() + "<---------");
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
        Files.walk(path)
                .filter(new Predicate<Path>() {
                    @Override
                    public boolean test(Path path) {
                        return matcher.matches(path);
                    }
                })
                .forEach(System.out::println);
    }
}
