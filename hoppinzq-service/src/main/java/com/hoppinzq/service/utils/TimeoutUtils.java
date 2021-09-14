package com.hoppinzq.service.utils;

import java.util.concurrent.*;

/**
 * @author:ZhangQi
 **/
public class TimeoutUtils {

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static <T> T process(Callable<T> task, long timeout) {
        if (task == null) {
            return null;
        }
        Future<T> futureRet = executor.submit(task);
        try {
            T ret = futureRet.get(timeout, TimeUnit.SECONDS);
            return ret;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            if (futureRet != null && !futureRet.isCancelled()) {
                futureRet.cancel(true);
            }
        }
        return null;
    }
}
