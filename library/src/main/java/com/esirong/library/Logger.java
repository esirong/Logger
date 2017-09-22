package com.esirong.library;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日志记录器
 * Created by 黄嵘才 on 2017/5/24.
 */

@SuppressWarnings("unused")
public final class Logger {
    private Logger() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static boolean debug = BuildConfig.DEBUG;
    private static SimpleDateFormat logFileFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// 日志文件格式
    private static SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 日志文件格式

    /**
     * 根据type输出日志消息，包括方法名，方法行数，msg
     *
     * @param type 日志类型
     * @param msg  msg
     */
    private static void log(int type, String msg) {
        try {
            log(type, msg, null);
        } catch (Exception ignore) {
            //ignore
        }
    }

    /**
     * 根据type输出日志消息，包括方法名，方法行数，msg，Throwable异常消息
     *
     * @param type 日志类型
     * @param msg  msg
     */
    private static void log(int type, String msg, Throwable tr) {
        if (!debug) {
            return;
        }
        try {
            StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[5];
            String className = stackTrace.getClassName();
            String tag = className.substring(className.lastIndexOf('.') + 1);
            msg = stackTrace.getMethodName() + "#" + stackTrace.getLineNumber() + " [" + msg + "]";
            switch (type) {
                case Log.DEBUG:
                    Log.d(tag, msg, tr);
                    break;
                case Log.INFO:
                    Log.i(tag, msg, tr);
                    break;
                case Log.WARN:
                    Log.w(tag, msg, tr);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg, tr);
                    break;
                case Log.VERBOSE:
                    Log.v(tag, msg, tr);
                    break;
            }
            if (debug && canWriteAble()) {
                writeLogFile(String.valueOf(type), tag, msg);
            }

        } catch (Exception ignore) {
            //ignore
        }

    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void d(String msg) {
        log(Log.DEBUG, msg);
    }

    public static void i(String msg) {
        log(Log.INFO, msg);
    }

    public static void w(String msg) {
        log(Log.WARN, msg);
    }

    public static void e(String msg) {
        log(Log.ERROR, msg);
    }

    public static void v(String msg) {
        log(Log.VERBOSE, msg);
    }

    public static void d(String msg, Throwable tr) {
        log(Log.DEBUG, msg, tr);
    }

    public static void i(String msg, Throwable tr) {
        log(Log.INFO, msg, tr);
    }

    public static void w(String msg, Throwable tr) {
        log(Log.WARN, msg, tr);
    }

    public static void e(String msg, Throwable tr) {
        log(Log.ERROR, msg, tr);
    }

    public static void v(String msg, Throwable tr) {
        log(Log.VERBOSE, msg, tr);
    }

    /**
     * 打开日志文件并写入日志
     **/
    private static void writeLogFile(String logType, String tag, String text) {// 新建或打开日志文件
        String sdcardDir = Environment.getExternalStorageDirectory().getAbsolutePath();// 日志文件在sdcard中的路径
        long now = System.currentTimeMillis();
        String needWriteFile = logFileFormat.format(now) + "Log.txt";// 本类输出的日志文件名称
        String needWriteMessage = logDateFormat.format(now) + "  " + logType + " " + tag + " " + text;
        File file = new File(sdcardDir, needWriteFile);
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException ignore) {
            //ignore
        }
    }

    private static boolean canWriteAble() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            Log.e("error", "sdcard未准备好");
            return false;
        }
    }
}
