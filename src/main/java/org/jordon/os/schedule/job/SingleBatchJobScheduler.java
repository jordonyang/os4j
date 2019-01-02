package org.jordon.os.schedule.job;

import org.jordon.os.entity.Job;
import org.jordon.os.constants.ScheduleAlgorithm;
import java.util.LinkedList;

/**
 * 单道批处理作业调度器
 */
public class SingleBatchJobScheduler extends JobScheduler{

    /**
     * 高响应比优先调度
     */
    private void highestResponseRadioNext() {
        // 将受调度作业数目
        int todoCount = todoQueue.size();
        for (int clock = 0; finishedQueue.size() < todoCount; clock++) {
            // 有待调度的作业，且作业在当前时钟到达
            if (todoQueue.size() > 0 && clock == todoQueue.get(0).getArrivalTime()) {
                // 作业在当前时钟到达，按照作业响应比从放入等待队列（队头作业响应比最高）
                int i = 0;
                Job head = todoQueue.pop();
                while (i < readyQueue.size()) {
                    if (readyQueue.get(i).getResponseRatio() < head.getResponseRatio()) {
                        readyQueue.add(i, head);
                        break;
                    }
                    i++;
                }
                if (i == readyQueue.size())
                    readyQueue.add(head);
            }
            // 有作业在等待
            if (readyQueue.size() > 0) {
                int needTime = readyQueue.get(0).getNeedTime();
                commonSchedule(clock);
                // 更新每个作业的响应比
                for (Job job : readyQueue) {
                    job.setWaitTime(job.getWaitTime() + needTime);
                    job.setResponseRatio(job.getWaitTime() * 1.0 / job.getNeedTime() + 1);
                }
            }
        }
        proccessFinishedQueue();
    }

    /**
     * 短作业优先
     */
    private void shortJobFirst() {
        // 将受调度作业数目
        int todoCount = todoQueue.size();
        for (int clock = 0; finishedQueue.size() < todoCount; clock++) {
            // 有待调度的作业，且作业在当前时钟到达
            if (todoQueue.size() > 0 && clock == todoQueue.get(0).getArrivalTime()) {
                // 作业在当前时钟到达，按照作业需要的时间大小从放入等待队列（队头作业需要的时间最小）
                int i = 0;
                Job head = todoQueue.pop();
                while (i < readyQueue.size()) {
                    if (readyQueue.get(i).getNeedTime() > head.getNeedTime()) {
                        readyQueue.add(i, head);
                        break;
                    }
                    i++;
                }
                if (i == readyQueue.size())
                    readyQueue.add(head);
            }

            // 有作业在等待
            if (readyQueue.size() > 0) {
                commonSchedule(clock);
            }
        }
        proccessFinishedQueue();
    }

    /**
     * 先来先服务, 每个作业占用CPU整个作业周期直到完成
     */
    private void firstComeFirstServed() {
        // 将受调度作业数目
        int todoCount = todoQueue.size();
        for (int clock = 0; finishedQueue.size() < todoCount; clock++) {
            // 有待调度的作业，且作业在当前时钟到达
            if (todoQueue.size() > 0 && clock == todoQueue.get(0).getArrivalTime()) {
                // 作业在当前时钟到达，放入等待队列
                readyQueue.add(todoQueue.pop());
            }
            // 有作业在等待
            if (readyQueue.size() > 0) {
                commonSchedule(clock);
            }
        }
        proccessFinishedQueue();
    }

    /**
     * 构造函数
     * @param todoQueue 任务队列
     * @param algorithm 调度算法
     */
    SingleBatchJobScheduler(LinkedList<Job> todoQueue, ScheduleAlgorithm algorithm) {
        super(todoQueue, algorithm, null);
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
            case HRRN:
                highestResponseRadioNext();
                break;
            default:
                throw new RuntimeException("scheduling algorithm does oot exist");
        }
    }
}
