package org.jordon.os.schedule.job;

import org.jordon.os.constants.SimulatedSystem;
import org.jordon.os.entity.Job;
import org.jordon.os.constants.ScheduleAlgorithm;
import org.junit.Test;
import java.util.LinkedList;

public class JobSchedulerTest {

    private JobScheduler scheduler;

    @Test
    public void scheduleSingleFCFS() {
        scheduler = new SingleBatchJobScheduler(prepareData(), ScheduleAlgorithm.FCFS);
        scheduler.schedule();
    }

    @Test
    public void scheduleSingleSJF() {
        scheduler = new SingleBatchJobScheduler(prepareData(), ScheduleAlgorithm.SJF);
        scheduler.schedule();
    }

    @Test
    public void scheduleSingleHRRN() {
        scheduler = new SingleBatchJobScheduler(prepareData(), ScheduleAlgorithm.HRRN);
        scheduler.schedule();
    }

    @Test
    public void scheduleMultiFCFS() {
        scheduler = new MultiBatchJobScheduler(SimulatedSystem.SIMULATED_RESOURCES,
                            prepareData(), ScheduleAlgorithm.FCFS);
        scheduler.schedule();
    }

    @Test
    public void scheduleMultiSJF() {
        scheduler = new MultiBatchJobScheduler(SimulatedSystem.SIMULATED_RESOURCES,
                            prepareData(), ScheduleAlgorithm.SJF);
        scheduler.schedule();
    }

    // 准备作业数据
    private static LinkedList<Job> prepareData() {
        LinkedList<Job> todoQueue = new LinkedList<>();
        Job job1 = new Job().setArrivalTime(0).setNeedTime(8).setName("job-1");
        Job job2 = new Job().setArrivalTime(2).setNeedTime(4).setName("job-2");
        Job job3 = new Job().setArrivalTime(4).setNeedTime(5).setName("job-3");
        todoQueue.add(job1);
        todoQueue.add(job2);
        todoQueue.add(job3);
        return todoQueue;
    }
}