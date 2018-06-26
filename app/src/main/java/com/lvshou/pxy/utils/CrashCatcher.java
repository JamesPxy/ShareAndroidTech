package com.lvshou.pxy.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @desc： Created by JamesPxy on 2018/6/26 15:07
 * 参考自：https://github.com/android-notes/Cockroach
 */
public class CrashCatcher {

    public interface ExceptionHandler {

        void handlerException(Thread thread, Throwable throwable);
    }

    private CrashCatcher() {
    }

    private static ExceptionHandler sExceptionHandler;
    private static Thread.UncaughtExceptionHandler sUncaughtExceptionHandler;
    private static boolean hasInstalled = false;//标记位，避免重复安装卸载

    /**
     * 当主线程或子线程抛出异常时会调用exceptionHandler.handlerException(Thread thread, Throwable throwable)
     * <p>
     * exceptionHandler.handlerException可能运行在非UI线程中。
     * <p>
     * 若设置了Thread.setDefaultUncaughtExceptionHandler则可能无法捕获子线程异常。
     *
     * 原理：https://github.com/android-notes/Cockroach/blob/master/%E5%8E%9F%E7%90%86%E5%88%86%E6%9E%90.md
     *
     * @param exceptionHandler
     */
    public static synchronized void install(ExceptionHandler exceptionHandler) {
        /*if(BuildConfig.DEBUG){
            //debug模式下不捕获异常 便于及时发现错误
            return;
        }*/
        if (hasInstalled) {
            return;
        }
        hasInstalled = true;
        sExceptionHandler = exceptionHandler;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
//                        Binder.clearCallingIdentity();
                        if (e instanceof QuitCrashCatcherException) {
                            return;
                        }
                        if (sExceptionHandler != null) {
                            sExceptionHandler.handlerException(Looper.getMainLooper().getThread(), e);
                        }
                    }
                }
            }
        });

        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (sExceptionHandler != null) {
                    sExceptionHandler.handlerException(t, e);
                }
            }
        });

    }

    public static synchronized void uninstall() {
        if (!hasInstalled) {
            return;
        }
        hasInstalled = false;
        sExceptionHandler = null;
        //卸载后恢复默认的异常处理逻辑，否则主线程再次抛出异常后将导致ANR，并且无法捕获到异常位置
        Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                throw new QuitCrashCatcherException("Quit CrashCatcher.....");//主线程抛出异常，迫使 while (true) {}结束
            }
        });
    }

    static final class QuitCrashCatcherException extends RuntimeException {
        public QuitCrashCatcherException(String message) {
            super(message);
        }
    }

}