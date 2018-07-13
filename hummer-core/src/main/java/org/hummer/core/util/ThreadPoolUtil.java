package org.hummer.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
    public final static ExecutorService COMMON_POOL = Executors.newFixedThreadPool(100);
}
