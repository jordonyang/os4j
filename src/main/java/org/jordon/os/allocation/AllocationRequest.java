package org.jordon.os.allocation;

import lombok.Data;

@Data
public class AllocationRequest {
    // 请求分配空间大小
    private int needSize;

    // 请求者名称
    private String requesterName;

    public AllocationRequest(int needSize, String requesterName) {
        this.needSize = needSize;
        this.requesterName = requesterName;
    }

    @Override
    public String toString() {
        return "分配请求[" +
                "需要内存: " + needSize +
                ", 请求者: " + requesterName + "]";
    }
}
