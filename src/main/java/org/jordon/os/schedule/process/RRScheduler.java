package org.jordon.os.schedule.process;

import org.jordon.os.entity.Process;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 轮转法可以是
 *      简单轮转法、
 *      可变时间片轮转法，
 *      或多队列轮转法。
 *
 * 简单轮转法基本思想：所有就绪进程按FCFS排成一个队列，总是把处理机分配给队首的进程，
 *                 各进程占用CPU的时间片相同。如果运行进程用完它的时间片后还未完成，
 *                 就把它送回到就绪队列的末尾，把处理机重新分配给队首的进程。直至所有的进程运行完毕。
 */
public class RRScheduler extends BaseScheduler {

    private ArrayList<Process> blockingQueue = new ArrayList<>();

    private ArrayList<Process> readyQueue = new ArrayList<>();

    public static void main(String[] args) {
        new RRScheduler().schedule();
    }

    // 时间轮转法
    @Override
    void schedule() {
        inQueue();
        for (Process process : readyQueue) {
            process.run();
            int timeSlice = 500;
            try {
                TimeUnit.MILLISECONDS.sleep(Math.min(timeSlice, process.getNeedTime()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (process.getNeedTime() > timeSlice) {
                blockingQueue.add(process);
                System.out.println(process.getName() + " is added in blockingQueue");
            }
        }
        readyQueue.clear();
    }

    private void inQueue() {
        for (int i = 0; i < 10; i++) {
            readyQueue.add(Process.getRandInstance(i));
        }
    }
}
