package com.dou361.pool;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网电子科技有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/11/11
 * <p/>
 * 描 述：收集错误日志
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class LogCatUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private Context context;

    public LogCatUncaughtExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        File path = new File(Environment.getExternalStorageDirectory()
                .getAbsoluteFile()
                + File.separator
                + context.getPackageName().substring(
                context.getPackageName().lastIndexOf(".") + 1));
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, SystemClock.uptimeMillis() + ".log");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        /** 出现错误后，直接杀死进程 */
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
