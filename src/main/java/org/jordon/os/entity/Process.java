package org.jordon.os.entity;

import lombok.Data;
import org.jordon.os.constants.ProcessStatus;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 进程控制实体类
 */
@Data
public class Process {

    // 数据段，仅作模逆，未使用
    private byte[] dataBytes;
    // 程序段，仅作模逆，未使用
    private byte[] programBytes;

    /******************* PCB信息 ********************/
    // 进程标识符
    private int id;
    // 进程名
    private String name;
    // 优先级
    private int priority;
    // 进程状态(1表示运行态，2表示就绪态，3表示阻塞态）
    // 就绪 W（Wait）、运行R（Run）、或完成F（Finish）
    private ProcessStatus status;
    // 进程执行总共需要用的时间
    private int needTime;
    // 进程目前已经运行时间
    private int runTime;
    // 到达时间
    private int arrivalTime;
    // 完成时间
    private int finishTime;
    /******************* PCB信息 ********************/

    public Process() {
        this.status = ProcessStatus.WAITING;
    }

    private void runOneSlice() {
        this.runTime ++;
        System.out.println(this + " runs a slice");
    }

    private void runSpecifySlice(int slices) {
        if (slices < 0 || slices > this.needTime) return;
        for (int i = 0; i < slices; i++) {
            runOneSlice();
        }
        System.out.println(this + " runs " + slices + " slice");
    }

    public void run() {
        for (int i = 0; i < this.getNeedTime(); i++) {
            try {
                Thread.sleep(100);
                runOneSlice();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", needTime=" + needTime +
                ", runTime=" + runTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    /**
     * 随机获取进程对象实例
     * @return Process对象
     */
    public static Process getRandInstance(int pid) {
        Process process = new Process();
        int bound = 10;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        process.setNeedTime(random.nextInt(bound) + 1);
        process.setPriority(random.nextInt(bound) + 1);
        process.setName("process-" + String.valueOf(pid));
        process.setId(pid);
        process.setStatus(ProcessStatus.WAITING);
        process.setArrivalTime(2 * pid);
        return process;
    }

    public static void main(String[] args) {
        for (int i=0; i < 10; i++) {
            System.out.println(getRandInstance(i));
        }
    }
}
