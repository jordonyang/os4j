package org.jordon.os.schedule.process.mfq;

import lombok.Data;
import org.jordon.os.entity.Process;
import java.util.LinkedList;
@Data
public class Queue {
    // 队列实体
    private LinkedList<Process> queue;
    // 队列中进程的时间片
    private int quantum;

    public Queue() {
        queue = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Queue{" +
                "queue=" + queue +
                ", slice=" + quantum +
                '}';
    }
}
