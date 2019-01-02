package org.jordon.os.schedule.process;

import org.jordon.os.entity.Process;

import java.util.ArrayList;

/**
 * 优先级调度算法(Priority-Scheduling Algorithm, PSA)
 * 基本思想：把CPU分配给就绪队列中优先数最高的进程。
 *
 * 静态优先数是在创建进程时确定的，并在整个进程运行期间不再改变。
 *
 * 动态优先数是指进程的优先数在创建进程时可以给定一个初始值，并且可以按一定原则修改优先数。
 * 例如：在进程获得一次CPU后就将其优先数减少1。
 *      或者，进程等待的时间超过某一时限时增加其优先数的值，等等。
 */
public class PSAScheduler extends BaseScheduler {

    private ArrayList<Process> readyQueue = new ArrayList<>();

    @Override
    void schedule() {
        inQueue();
        for (Process process : readyQueue) {
            process.run();
        }
        readyQueue.clear();
    }

    private void inQueue() {
        for (int i = 0; i < 10; i++) {
            readyQueue.add(Process.getRandInstance(i));
        }
        System.out.println("origin queue");
        print();
        sort();
        System.out.println("sorted queue");
        print();
        System.out.println();
    }

    public static void main(String[] args) {
        new PSAScheduler().schedule();
    }



    private void sort() {
        for (int i = 0; i < readyQueue.size(); i++) {
            for (int j = i + 1; j < readyQueue.size(); j++) {
                if (readyQueue.get(i).getPriority() < readyQueue.get(j).getPriority()) {
                    Process temp = readyQueue.get(i);
                    readyQueue.set(i, readyQueue.get(j));
                    readyQueue.set(j, temp);
                }
            }
        }
    }

    private void print() {
        for (Process process : readyQueue) {
            System.out.print(process + " ");
        }
        System.out.println("\n");
    }
}
