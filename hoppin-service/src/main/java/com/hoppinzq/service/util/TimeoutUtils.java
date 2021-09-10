package com.hoppinzq.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author:ZhangQi
 **/
public class TimeoutUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutUtils.class);

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
            LOGGER.error("Interrupt Exception", e);
        } catch (ExecutionException e) {
            LOGGER.error("Task execute exception", e);
        } catch (TimeoutException e) {
            LOGGER.warn("Process Timeout");
            if (futureRet != null && !futureRet.isCancelled()) {
                futureRet.cancel(true);
            }
        }
        return null;
    }
}
