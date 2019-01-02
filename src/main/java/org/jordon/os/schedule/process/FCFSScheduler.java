package org.jordon.os.schedule.process;

import org.jordon.os.constants.ProcessStatus;
import org.jordon.os.entity.Process;

import java.util.LinkedList;

/**
 * 先来先服务（first-come first-served）调度算法
 * 应用于作业调度和进程调度
 * 1. 作业调度：
 *      系统按照作业到达的先后次序进行调度
 * 2. 进程调度：
 *      从就绪的进程队列中选择最先入队的进程，为之分配处理机
 */
public class FCFSScheduler extends BaseScheduler {

    // 就绪队列
    private LinkedList<Process> readyQueue;

    public FCFSScheduler(LinkedList<Process> readyQueue) {
        this.readyQueue = readyQueue;
    }

    @Override
    public void schedule() {
        Process process;
        while ((process = readyQueue.pollFirst()) != null &&
                process.getStatus().equals(ProcessStatus.WAITING)) {
            process.run();
        }
    }

    public static void main(String[] args) {
        LinkedList<Process> readyQueue = new LinkedList<>();
        for (int i=0; i < 10; i++) {
            readyQueue.add(Process.getRandInstance(i));
        }
        new FCFSScheduler(readyQueue).schedule();
    }
}
