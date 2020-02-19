package one.peace.oneplayer.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void submit(Runnable runnable) {
        executorService.submit(runnable);

    }
}
