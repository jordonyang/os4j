package org.jordon.os.allocation;

import org.jordon.os.constants.AllocationAlgorithm;
import org.jordon.os.entity.Partition;
import org.jordon.os.entity.Process;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 动态分区分配器
 */
public class DynamicPartitionAllocator extends PartitionAllocator {

    // 分配算法
    private AllocationAlgorithm algorithm;

    // 控制分配的分区链
    private LinkedList<Partition> partitions;

    // 循环首次适应算法的起始查询指针，用于记录上一次分配的分区位置
    private int beginPoint;

    private static final int size = 1;

    private ListIterator<Partition> iterator;

    private LinkedList<Partition> copy;

    public DynamicPartitionAllocator(AllocationAlgorithm algorithm,
                                     LinkedList<Partition> partitions) {
        this.algorithm = algorithm;
        this.partitions = partitions;
        if (algorithm == AllocationAlgorithm.NF) {
            iterator = partitions.listIterator();
        }
        copy = new LinkedList<>();
    }

    public boolean free(String occupierName) {
        System.out.println("请求释放[" + occupierName + "]的分区\t\t释放前的分区情况");
        printPartitions(partitions);
        Partition toBeFree = null;
        for (Partition partition : partitions) {
            if (occupierName.equals(partition.getOccupierName())) {
                toBeFree = partition;
            }
        }
        if (toBeFree != null) {
            int index = partitions.indexOf(toBeFree);
            Partition next = partitions.get(index + 1);
            if (!next.isAllocated()) { // 与后一个空闲区邻接，合并
                next.setTotalSize(next.getTotalSize() + toBeFree.getTotalSize());
                next.setStartAddress(toBeFree.getStartAddress());
                partitions.remove(toBeFree);
                printFreeMsg();
                return true;
            }
            if (index != 0) {   // 如果不是第一个，向前找
                Partition last = partitions.get(index - 1);
                if (!last.isAllocated()) { // 与前一个空闲区邻接，合并
                    last.setTotalSize(last.getTotalSize() + toBeFree.getTotalSize());
                    partitions.remove(toBeFree);
                    printFreeMsg();
                    return true;
                }
            }
            // 前后都没有空闲分区，自己成为一个空闲分区
            toBeFree.setAllocatedSize(0);
            toBeFree.setOccupierName(null);
            toBeFree.setId(0);
            toBeFree.setAllocated(false);
            printFreeMsg();
            return true;
        }
        return false;
    }

    private void printFreeMsg() {
        System.out.println("释放后的分区情况");
        printPartitions(partitions);
        System.out.println();
    }

    // 首次适应，顺序查找，直到找到一个大小能满足作业要求的空闲分区
    private boolean firstFit(AllocationRequest request) {
        boolean flag = false;
        System.out.println(request + "\t\t分配前的分区情况");
        printPartitions(partitions);
        int needSize = request.getNeedSize();
        if (partitions.size() > 0) {
            ListIterator<Partition> iterator = partitions.listIterator();

            while (iterator.hasNext()) {
                Partition partition = iterator.next();
                int totalSize = partition.getTotalSize();

                int lastSIze = totalSize - needSize;
                if (!partition.isAllocated() && lastSIze > 0) {
                    if (lastSIze > size) { // 新增空白分区
                        Partition newPartition =
                                new Partition(partitions.size() + 1, lastSIze);
                        newPartition.setStartAddress(partition.getStartAddress() + needSize);
                        newPartition.setName("-" + partition.getName());
                        iterator.add(newPartition);
                        partition.setTotalSize(needSize);
                    }
                    // 将当前分区分给请求者
                    partition.setAllocatedSize(needSize);
                    partition.setAllocated(true);
                    partition.setOccupierName(request.getRequesterName());
                    flag = true;
                    break;
                }
            }
        }
        String msg = "分配" + (flag ? "成功" : "失败");
        System.out.println(msg + "\t\t分配后的分区情况");
        printPartitions(partitions);
        System.out.println();
        return flag;
    }

    // 循环首次适应，设置起始查询指针记录上一次分配的分区位置
    private boolean nextFit(AllocationRequest request) {
        return false;
    }

    /**
     * 根据作业大小排序
     */
    private void sort() {
        for (int i = 0; i < copy.size(); i++) {
            for (int j = i + 1; j < copy.size(); j++) {
                if (copy.get(i).getTotalSize() > copy.get(j).getTotalSize()) {
                    Partition temp = copy.get(i);
                    copy.set(i, copy.get(j));
                    copy.set(j, temp);
                }
            }
        }
    }
    private void copy() {
        copy = new LinkedList<>();
        copy.addAll(partitions);
    }

    // 最佳适应，将最小的满足大小要求的空闲分区分配给请求者
    private boolean bestFit(AllocationRequest request) {
        int needSize = request.getNeedSize();
        boolean flag = false;
        System.out.println(request + "\t\t分配前的分区情况");
        printPartitions(partitions);
        copy();
        sort();
        int start = 0;
        for (Partition partition : partitions) {
            int lastSIze = partition.getTotalSize() - needSize;
            if (!partition.isAllocated() && lastSIze > 0) {
                start = partition.getStartAddress();
            }
        }

        if (partitions.size() > 0) {
            ListIterator<Partition> iterator = partitions.listIterator();

            while (iterator.hasNext()) {
                Partition partition = iterator.next();
                int totalSize = partition.getTotalSize();

                int lastSIze = totalSize - needSize;
                if (partition.getStartAddress() == start) {
                    if (lastSIze > size) { // 新增空白分区
                        Partition newPartition =
                                new Partition(partitions.size() + 1, lastSIze);
                        newPartition.setStartAddress(partition.getStartAddress() + needSize);
                        newPartition.setName("-" + partition.getName());
                        iterator.add(newPartition);
                        partition.setTotalSize(needSize);
                    }
                    // 将当前分区分给请求者
                    partition.setAllocatedSize(needSize);
                    partition.setAllocated(true);
                    partition.setOccupierName(request.getRequesterName());
                    flag = true;

                    sort();
                    break;
                }
            }
        }
        String msg = "分配" + (flag ? "成功" : "失败");
        System.out.println(msg + "\t\t分配后的分区情况");
        printPartitions(partitions);
        System.out.println();
        return flag;
    }

    private boolean worstFit(AllocationRequest request) {
        return false;
    }

    public boolean allocate(AllocationRequest request) {
        switch (algorithm) {
            case FF:
                return firstFit(request);
            case NF:
                return nextFit(request);
            case BF:
                return bestFit(request);
            case WF:
                return worstFit(request);
            default:
                throw new RuntimeException("allocation algorithm does not exist");
        }
    }
}
