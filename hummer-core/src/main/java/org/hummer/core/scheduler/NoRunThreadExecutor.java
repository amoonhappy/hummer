package org.hummer.core.scheduler;

import org.quartz.spi.ThreadExecutor;

public class NoRunThreadExecutor implements ThreadExecutor {

    public void initialize() {
    }

    public void execute(Thread thread) {
        // do nothing;
    }
}
