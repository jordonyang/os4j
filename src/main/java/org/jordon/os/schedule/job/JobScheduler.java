package org.jordon.os.schedule.job;

import org.jordon.os.entity.Job;
import org.jordon.os.constants.JobStatus;
import org.jordon.os.constants.ScheduleAlgorithm;
import org.jordon.os.schedule.Scheduler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class JobScheduler implements Scheduler {
    // 初始作业队列，模逆动态的作业到达
    LinkedList<Job> todoQueue;
    // 等待队列
    LinkedList<Job> readyQueue;
    // 完成队列
    LinkedList<Job> finishedQueue;
    // 调度算法
    ScheduleAlgorithm algorithm;
    // 系统资源
    int[] resources;

    JobScheduler(LinkedList<Job> todoQueue, ScheduleAlgorithm algorithm, int[] resources) {
        this.todoQueue = todoQueue;
        this.algorithm = algorithm;
        this.resources = resources;
        readyQueue = new LinkedList<>();
        finishedQueue = new LinkedList<>();
        System.out.println("初始作业队列");
        printJobs(todoQueue);
    }

    /**
     * 一个时钟周期内的作业调度
     * @param clock 时钟周期
     */
    void commonSchedule(int clock) {
        System.out.println("\nt[" + clock + "]");
        printJobs(readyQueue);
        Job head = readyQueue.get(0);
        if (head.getRunTime() == 0) {
            head.setStartTime(clock);
        }
        head.setStatus(JobStatus.RUNNING);
        head.setRunTime(head.getRunTime() + 1);
        head.setNeedTime(head.getNeedTime() - 1);

        if (head.getNeedTime() == 0) { // 作业完成
            head.setStatus(JobStatus.FINISH);
            head.setFinishTime(clock);
            head.setTurnaroundTime(head.getFinishTime() - head.getArrivalTime());
            head.setWeightedTurnaroundTime(head.getTurnaroundTime() * 1.0 / head.getRunTime());
            finishedQueue.add(head);
            readyQueue.remove(head);
        }
    }

    /**
     * 处理完成作业队列，如打印其中的作业信息，计算平均数据
     */
    void proccessFinishedQueue() {
        // 将受调度作业数目
        System.out.println("\n完成作业队列");
        printJobs(finishedQueue);
        int finishedCount = finishedQueue.size();
        // 计算平均周转时间及带权周转时间
        double turnaroundTimeSum = 0, weightedTurnaroundTimeSum = 0;
        for (Job job : finishedQueue) {
            turnaroundTimeSum += job.getTurnaroundTime();
            weightedTurnaroundTimeSum += job.getWeightedTurnaroundTime();
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.println(String.format("%-9s%-10s%-10s%-10s%-10s", "作业数目",
                "总周转时间", "总带权周转时间", "平均周转时间", "平均总带权周转时间"));
        System.out.println(String.format("%-13s%-14s%-13s%-15s%-14s", finishedCount,
                turnaroundTimeSum, weightedTurnaroundTimeSum, String.format("%.3f", turnaroundTimeSum / finishedCount),
                String.format("%.3f", weightedTurnaroundTimeSum / finishedCount)));
        System.out.println("-------------------------------------------------------------------");
    }

    /**
     * 格式化打印单个作业信息
     * @param job 作业对象
     */
    private void printJob(Job job) {
        System.out.println(String.format("%-14s%-12s%-13s%-10s%-11s%-15s%-1s", job.getName(),
                job.getArrivalTime(), job.getNeedTime(), job.getRunTime(),
                String.format("%.3f", job.getResponseRatio()),
                Arrays.toString(job.getNeedResources()),
                Arrays.toString(resources)));
    }

    /**
     * 格式化打印作业集信息
     * @param jobs 作业集
     */
    void printJobs(List<Job> jobs) {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s%-11s%-10s", "作业名",
                "到达时间", "所需时间", "运行时间", "响应比", "所需资源", "系统资源"));
        for (Job job : jobs) {
            printJob(job);
        }
        System.out.println("------------------------------------------------------------------------------------");
    }
}
