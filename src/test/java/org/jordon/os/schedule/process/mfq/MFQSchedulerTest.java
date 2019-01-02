package org.jordon.os.schedule.process.mfq;

import org.junit.Test;

import static org.junit.Assert.*;

public class MFQSchedulerTest {

    private MFQScheduler scheduler = new MFQScheduler();

    @Test
    public void schedule() {
        scheduler.schedule();
    }
}