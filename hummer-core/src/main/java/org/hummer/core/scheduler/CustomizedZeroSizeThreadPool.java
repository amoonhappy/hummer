package org.hummer.core.scheduler;

import org.quartz.simpl.ZeroSizeThreadPool;

public class CustomizedZeroSizeThreadPool extends ZeroSizeThreadPool {

    private int threadCount;

    private int threadPriority;

    private boolean inheritLoader;

    public CustomizedZeroSizeThreadPool() {
        super();
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public boolean isThreadsInheritContextClassLoaderOfInitializingThread() {
        return this.inheritLoader;
    }

    public void setThreadsInheritContextClassLoaderOfInitializingThread(boolean inheritLoader) {
        this.inheritLoader = inheritLoader;
    }

}