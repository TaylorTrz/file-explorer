package com.taylor.tooz;

import com.taylor.tooz.utils.format.DocFormatUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import static com.taylor.tooz.utils.LogUtil.log;


public class Main {
    // TODO: lazy-loading enter func...
    // index, function name, more specified note, function entrance
    private static final String[] functors = new String[]{
            "0", "Exit", "退出!", "com.taylor.tooz.Main.exit",
            "1", "File-Explorer", "文件目录管理器v1.0", "com.taylor.tooz.explorer.Explorer.start",
            "2", "Duplicated-File-Checker", "重复文件查杀器v1.0", "com.taylor.tooz.killer.DoubleKiller.start",
            "3", "Git-Helper", "Git使用帮助器", "com.taylor.tooz.git.EmptyDirHelper.start",
            "4", "Bytecode-Reader", "字节码阅读提示器", "com.taylor.tooz.bytecode.ReadHelper.start"
    };

    private static Map<String, String[]> functorRef = new ConcurrentHashMap<>(10);

    public static void run() {
        DocFormatUtil.DocBuilder msg = new DocFormatUtil().getBuilder(0, 0);
        for (int ref = 0; ref < functors.length; ref += 4) {
            functorRef.put(functors[ref],
                    new String[]{functors[ref + 1], functors[ref + 2], functors[ref + 3]});
            msg.then(functors[ref]).then(functors[ref + 1]).enter();
        }

        String welcomeScreen = msg.build();
        while (true) {
            System.out.println(welcomeScreen);
            String input = new Scanner(System.in).nextLine();
            if (input == null || !functorRef.containsKey(input)) {
                continue;
            }
            String[] args = functorRef.get(input);
            System.out.println(args[1]);
            String cName = args[2].substring(0, args[2].lastIndexOf('.'));
            String mName = args[2].substring(args[2].lastIndexOf('.') + 1);
            try {
                Class clazz = Class.forName(cName);
                Method method = clazz.getDeclaredMethod(mName);
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void exit() {
        try {
            log.flush();
            log.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().exit(0);
        }
    }


    public static void main(String[] args) {
        run();
    }
}
