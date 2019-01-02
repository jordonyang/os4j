package org.jordon.os.constants;

public enum ScheduleAlgorithm {

    FCFS("FCFS"),   // 先来先服务
    SJF("SJF"),   // 短作业优先
    HRRN("HRRN"),    // 高响应比优先
    ;

    private String name;

    ScheduleAlgorithm(String code) {
        this.name = name;
    }
}
