package org.jordon.os.constants;

public enum JobStatus {
    WAITING(0),   // 等待
    RUNNING(1),   // 运行
    FINISH(2),    // 完成
    ;

    private int code;

    JobStatus(int code) {
        this.code = code;
    }
}
