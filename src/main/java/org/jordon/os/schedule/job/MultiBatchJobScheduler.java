package org.jordon.os.schedule.job;

import org.jordon.os.constants.JobStatus;
import org.jordon.os.entity.Job;
import org.jordon.os.constants.ScheduleAlgorithm;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 多道批处理作业调度器
 * 1. 分别采用先来先服务（FCFS）和短作业优先调度算法。
 * 2. 假定系统中具有的各种资源及数量，调度作业时必须考虑到每个作业的资源要求，所需要的资源是否得到满足
 * 3. 从输入井选择若干个作业进入主存，若现有的尚未分配的资源可满足该作业的资源要求，则为它们分配必要的资源
 * 4. 当作业进入完成状态，系统将收回该作业所占用的全部资源，并清除有关的JCB
 *
 * 假定所有作业请求一定数量的同一资源
 */
public class MultiBatchJobScheduler extends JobScheduler {

    // 记录时钟到达的作业，只有在此队列中满足条件的才能进入readyQueue
    private LinkedList<Job> waitingQueue;

    public MultiBatchJobScheduler(int[] resources,
        LinkedList<Job> todoQueue, ScheduleAlgorithm algorithm) {
        super(todoQueue, algorithm, resources);
        waitingQueue = new LinkedList<>();
    }

    /**
     * 先来先服务
     */
    private void firstComeFirstServed() {
        // 将受调度作业数目
        int todoCount = todoQueue.size();
        for (int clock = 0; finishedQueue.size() < todoCount; clock++) {
            // 有待调度的作业，且作业在当前时钟到达
            if (todoQueue.size() > 0 && clock == todoQueue.get(0).getArrivalTime()) {
                // 作业在当前时钟到达，放入等待队列
                waitingQueue.add(todoQueue.pop());
            }

            // 只有在此队列中满足条件的才能进入readyQueue
            Iterator iterator = waitingQueue.iterator();
            while (iterator.hasNext()) {
                Job job = (Job) iterator.next();
                if (areSufficient(job)) {
                    readyQueue.add(job);
                    // 更新资源信息
                    for (int j = 0; j < job.getNeedResources().length; j++) {
                        resources[j] -= job.getNeedResources()[j];
                    }
                    iterator.remove();
                }
            }

            // 有作业在等待
            if (readyQueue.size() > 0) {
                batchSchedule(clock);
            }
        }
        proccessFinishedQueue();
    }

    /**
     * clock 时刻的调度
     * @param clock 某个时钟信号
     */
    private void batchSchedule(int clock) {
        System.out.println("\nt[" + clock + "]");
        printJobs(readyQueue);

        Iterator iterator = readyQueue.iterator();
        while (iterator.hasNext()) {
            Job job = (Job) iterator.next();
            job.setStatus(JobStatus.RUNNING);
            job.setRunTime(job.getRunTime() + 1);
            job.setNeedTime(job.getNeedTime() - 1);

            if (job.getNeedTime() == 0) { // 作业完成
                job.setStatus(JobStatus.FINISH);
                job.setFinishTime(clock);
                job.setTurnaroundTime(job.getFinishTime() - job.getArrivalTime());
                job.setWeightedTurnaroundTime(job.getTurnaroundTime() * 1.0 / job.getRunTime());

                for (int j = 0; j < job.getNeedResources().length; j++) {
                    resources[j] += job.getNeedResources()[j];
                }
                finishedQueue.add(job);
                iterator.remove();
            }
        }
    }

    /**
     * 判断资源对于某个作业而言是否够用
     * @param job 某个作业
     * @return 够用返回true
     */
    private boolean areSufficient(Job job) {
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] < job.getNeedResources()[i])
                return false;
        }
        return true;
    }

    /**
     * 最短作业优先
     */
    private void shortJobFirst() {
        // 将受调度作业数目
        int todoCount = todoQueue.size();
        for (int clock = 0; finishedQueue.size() < todoCount; clock++) {
            // 有待调度的作业，且作业在当前时钟到达
            if (todoQueue.size() > 0 && clock == todoQueue.get(0).getArrivalTime()) {
                // 有待调度的作业，且作业在当前时钟到达
                int i = 0;
                Job head = todoQueue.pop();
                while (i < waitingQueue.size()) {
                    if (waitingQueue.get(i).getNeedTime() > head.getNeedTime()) {
                        waitingQueue.add(i, head);
                        break;
                    }
                    i++;
                }
                if (i == waitingQueue.size())
                    waitingQueue.add(head);
            }

            // 只有在此队列中满足条件的才能进入readyQueue
            Iterator iterator = waitingQueue.iterator();
            while (iterator.hasNext()) {
                Job job = (Job) iterator.next();
                if (areSufficient(job)) {
                    readyQueue.add(job);
                    // 更新资源信息
                    for (int j = 0; j < job.getNeedResources().length; j++) {
                        resources[j] -= job.getNeedResources()[j];
                    }
                    iterator.remove();
                }
            }

            // 有作业在等待
            if (readyQueue.size() > 0) {
                batchSchedule(clock);
            }
        }
        proccessFinishedQueue();
    }

    @Override
    public void schedule() {
        switch (algorithm) {
            case FCFS:
                firstComeFirstServed();
                break;
            case SJF:
                shortJobFirst();
                break;
            default:
                throw new RuntimeException("scheduling algorithm does oot exist");
        }
    }
}
