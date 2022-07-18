package com.hoppinzq.service.log;

import java.io.*;
import java.util.Date;

/**
 * @author: zq
 */
public class Log {
    public static final int LOG_LEVEL_DUMP = 1;
    public static final int LOG_LEVEL_TRACE = 2;
    public static final int LOG_LEVEL_NORMAL = 3;
    public static final int LOG_LEVEL_ERROR = 4;
    public static final int LOG_LEVEL_NONE = 5;
    protected static boolean _log2console = true;
    protected static boolean _log2file = false;
    protected static String _path;
    protected static int _level;

    private Log() {
    }

    public static void setLevel(int var0) {
        if (var0 != 2 && var0 != 3 && var0 != 5 && var0 != 1 && var0 != 4) {
            _level = 3;
        } else {
            _level = var0;
        }

    }

    public static void setPath(String var0) {
        _path = var0;
    }

    public static void setFile(boolean var0) {
        _log2file = var0;
    }

    public static void setConsole(boolean var0) {
        _log2console = var0;
    }

    public static int getLevel() {
        return _level;
    }

    public static String getPath() {
        return _path;
    }

    public static boolean getConsole() {
        return _log2console;
    }

    public static boolean getFile() {
        return _log2file;
    }

    public static void logException(String var0, Exception var1) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        PrintStream var3 = new PrintStream(var2);
        var1.printStackTrace(var3);
        var3.close();
        log(4, var0 + var1 + ":" + var2);

        try {
            var2.close();
        } catch (IOException var5) {
        }

    }

    public static synchronized void log(int var0, String var1) {
        if (var0 != 5) {
            if (var0 >= _level) {
                Date var2 = new Date();
                String var3 = "[" + var2.toString() + "] [";
                switch(var0) {
                    case 1:
                        var3 = var3 + "DUMP";
                        break;
                    case 2:
                        var3 = var3 + "TRACE";
                        break;
                    case 3:
                        var3 = var3 + "NORMAL";
                        break;
                    case 4:
                        var3 = var3 + "ERROR";
                        break;
                    case 5:
                        var3 = var3 + "NONE?";
                }

                var3 = var3 + "][" + Thread.currentThread().getName() + "] " + var1;
                if (_log2console) {
                    System.out.println(var3);
                }

                if (_log2file) {
                    try {
                        FileOutputStream var4 = new FileOutputStream(_path, true);
                        PrintStream var5 = new PrintStream(var4);
                        var5.println(var3);
                        var5.close();
                        var4.close();
                    } catch (IOException var6) {
                    }
                }

            }
        }
    }

    static {
        _path = "." + File.pathSeparator + "log.txt";
        _level = 5;
    }
}

