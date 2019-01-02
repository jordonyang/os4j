package org.jordon.os.allocation;

import org.jordon.os.entity.Partition;
import java.util.List;

public abstract class PartitionAllocator {

    public abstract boolean allocate(AllocationRequest request);

    /**
     * 格式化打印单个分区信息
     * @param partition 分区对象
     */
    private void printPartition(Partition partition) {
        System.out.println(String.format("%-19s%-13s%-13s%-14s%-14s%-12s%-10s", partition.getName(),
                partition.getId(), partition.getTotalSize(), partition.isAllocated(),
                partition.getStartAddress(),
                partition.getAllocatedSize(), partition.getOccupierName()));
    }

    /**
     * 格式化打印分区集信息
     * @param partitions 分区集
     */
    void printPartitions(List<Partition> partitions) {
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println(String.format("%-14s%-10s%-10s%-10s%-10s%-10s%-10s", "   分区名",
                "分区标记", "分区总大小", "是否占用", "起始地址", "已分配大小", "占用者"));
        for (Partition partition : partitions) {
            printPartition(partition);
        }
        System.out.println("-------------------------------------------------------------------------------------------");
    }
}
