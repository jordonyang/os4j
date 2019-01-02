package org.jordon.os.entity;

import lombok.Data;

/**
 * 内存分区实体
 */
@Data
public class Partition {
    // 分区唯一标记
    private int id;

    // 分区名
    private String name;

    // 起始地址
    private int startAddress;

    // 分区大小
    private int totalSize;

    // 分区是否已分配给作业
    private boolean isAllocated;

    // 已分配的大小
    private int allocatedSize;

    // 占用者名字，可以是进程或作业
    private String occupierName;

    public Partition(int id, int totalSize) {
        this.id = id;
        this.totalSize = totalSize;
        setName("partition-" + String.valueOf(id));
    }

    public void setId(int id) {
        this.id = id;
        setName("partition-" + String.valueOf(id));
    }
}
