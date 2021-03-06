package org.jordon.os.schedule.process.mfq;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jordon.os.constants.ProcessStatus;
import org.jordon.os.entity.Process;

/**
 * 多级反馈队列
 */
@Getter
public class MFQScheduler {

    // 设置多个就绪队列，为每个就绪队列赋不同的优先级
    private List<Queue> queues;
    private List<Process> processes;

    public MFQScheduler() {
        queues = new ArrayList<>();
        processes = ranProcesses();
        buildQueues();
    }

    private void buildQueues() {
        // 获取待处理进程中进程的最大的时间片
        int maxSlice = 0;
        for (Process process : processes) {
            int needTime = process.getNeedTime();
            if (maxSlice < needTime) {
                maxSlice = needTime;
            }
        }

        // 为不同的设置时间片，高优先级队列时间片短
        int n = 1;
        while ((1 << n) - 1 < maxSlice) {
            n++;
        }
        for (int i = 0; i < n; i++) {
            Queue queue = new Queue();
            // 优先级递增 （序列为： 1, 2, 4, 8, 16）
            queue.setQuantum(1 << i);
            queues.add(i, queue);
        }
    }

    private List<Process> ranProcesses() {
        List<Process> processes = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int processCount = random.nextInt(20) + 1;
        for (int i = 0; i < 3; i++) {
            processes.add(Process.getRandInstance(i));
        }
        return processes;
    }

    public void schedule() {
        System.out.println("初始进程列表");
        printProcesses(processes);
        System.out.println();

        List<Process> finishedProcesses = new ArrayList<>();
        int originSize = processes.size();

        for (int clock = 0; finishedProcesses.size() != originSize; clock++) {
            String timeTag = "[t" + clock + ", ";
            Queue firstQueue = queues.get(0);

            if (processes.size() > 0) {
                Process firstProcess = processes.get(0);
                // 进程到达，将它放到第一条服务队列，并从原等待队列中弹出
                if (clock == firstProcess.getArrivalTime()) {
                    firstQueue.getQueue().add(firstProcess);
                    processes.remove(firstProcess);
                }
            }

            for (int i = 0; i < queues.size(); i++) {
                Queue queue = queues.get(i);
                System.out.println(timeTag + "queue" + i + ", slice: "+ queue.getQuantum() + "]");

                if (queue.getQueue().size() == 0) continue;
                printProcesses(queue.getQueue());
                System.out.println();
                // 先来先服务
                Process head = queue.getQueue().get(0);
                // .....服务
                head.setStatus(ProcessStatus.RUNNING);
                // 更新运行时间
                int needTime = head.getNeedTime() - 1;
                head.setRunTime(head.getRunTime() + 1);

                // 更新需要时间
                head.setNeedTime(needTime);
                head.setStatus(ProcessStatus.WAITING);

                // 进程完成
                if (needTime <= 0) {
                    head.setFinishTime(clock);
                    head.setStatus(ProcessStatus.FINISH);
                    finishedProcesses.add(head);
                    queue.getQueue().remove(head);
                }
                // 进程仍旧需要运行，插入下一个队列的尾部
                else if (head.getRunTime() >= queue.getQuantum()) {
                    head.setRunTime(0);
                    // 插入到下一个队列的尾部
                    queues.get(i + 1).getQueue().add(head);
                    queue.getQueue().remove(head);
                }
                // 结束，等待下一个时间片
                break;
            }
        }
    }

    /**
     * 格式化打印单个进程信息
     * @param process 进程对象
     */
    private void printProcess(Process process) {
        System.out.println(String.format("%-19s%-12s%-13s%-1s", process.getName(),
                process.getArrivalTime(), process.getNeedTime(), process.getRunTime()));
    }

    /**
     * 格式化打印进程集信息
     * @param processes 进程集
     */
    private void printProcesses(List<Process> processes) {
        System.out.println("-------------------------------------------------");
        System.out.println(String.format("%-15s%-10s%-10s%-10s", "  进程名",
                "到达时间", "所需时间", "运行时间"));
        for (Process process : processes) {
            printProcess(process);
        }
        System.out.println("-------------------------------------------------");
    }
}
