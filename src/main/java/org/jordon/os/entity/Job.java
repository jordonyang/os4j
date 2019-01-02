package org.jordon.os.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jordon.os.constants.JobStatus;
import org.jordon.os.constants.SimulatedSystem;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 作业实体
 */
@Data
@Accessors(chain = true)
public class Job {
    // 作业标识
    private int id;
    // 作业名
    private String name;
    // 到达时间
    private int arrivalTime;
    // 所需时间
    private int needTime;
    // 开始时间
    private int startTime;
    // 已经占用CPU时间
    private int runTime;
    // 等待时间
    private int waitTime;
    // 完成时间
    private int finishTime;
    // 周转时间 = 完成时间 - 到达时间
    private int turnaroundTime;
    // 带权周转时间
    private double weightedTurnaroundTime ;
    // 所需资源
    private int[] needResources;
    // 作业状态
    private JobStatus status;
    // 响应比
    private double responseRatio;

    /**
     * 设置初始化信息
     */
    public Job() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        needResources = new int[SimulatedSystem.SIMULATED_RESOURCES.length];
        // 初始化所需资源数量（模拟）
        for (int i = 0; i < needResources.length; i++) {
            needResources[i] = random.nextInt(SimulatedSystem.SIMULATED_RESOURCES[i]);
        }
        status =  JobStatus.WAITING;
    }

    /**
     * 获取随机作业实例
     * @param jobId 作业id
     * @return Job对象
     */
    public static Job getRandInstance(int jobId) {
        Job job = new Job();
        int bound = 10;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        job.setId(jobId);
        job.setNeedTime(random.nextInt(bound) + 1);
        job.setName("job-" + String.valueOf(jobId));
        job.setId(jobId);
        job.setStatus(JobStatus.WAITING);
        return job;
    }
}
