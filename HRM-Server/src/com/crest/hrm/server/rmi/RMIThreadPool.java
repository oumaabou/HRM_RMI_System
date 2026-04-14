package com.crest.hrm.server.rmi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIThreadPool {

    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static ExecutorService getExecutor() {
        return executor;
    }
}
