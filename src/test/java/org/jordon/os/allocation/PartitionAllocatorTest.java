package org.jordon.os.allocation;

import org.jordon.os.constants.AllocationAlgorithm;
import org.junit.Test;

import java.util.LinkedList;
import org.jordon.os.entity.Partition;

public class PartitionAllocatorTest {
    private DynamicPartitionAllocator allocator;

    @Test
    public void testBF() {
        LinkedList<Partition> partitions = preparePartitions();
        allocator = new DynamicPartitionAllocator(AllocationAlgorithm.BF, partitions);
        LinkedList<AllocationRequest> requests = prepareRequests();
        for (AllocationRequest request : requests) {
            allocator.allocate(request);
        }
        allocator.free("作业2");
        AllocationRequest request4 = new AllocationRequest(200, "作业4");
        allocator.allocate(request4);
        allocator.free("作业3");
        allocator.free("作业1");
        AllocationRequest request5 = new AllocationRequest(100, "作业5");
        allocator.allocate(request5);
        AllocationRequest request6 = new AllocationRequest(60, "作业6");
        allocator.allocate(request6);
        AllocationRequest request7 = new AllocationRequest(50, "作业7");
        allocator.allocate(request7);
    }

    @Test
    public void testFF() {
        LinkedList<Partition> partitions = preparePartitions();
        allocator = new DynamicPartitionAllocator(AllocationAlgorithm.FF, partitions);
        LinkedList<AllocationRequest> requests = prepareRequests();
        for (AllocationRequest request : requests) {
            allocator.allocate(request);
        }
        allocator.free("作业2");
        AllocationRequest request4 = new AllocationRequest(200, "作业4");
        allocator.allocate(request4);
        allocator.free("作业3");
        allocator.free("作业1");
        AllocationRequest request5 = new AllocationRequest(140, "作业5");
        allocator.allocate(request5);
        AllocationRequest request6 = new AllocationRequest(60, "作业6");
        allocator.allocate(request6);
        AllocationRequest request7 = new AllocationRequest(50, "作业7");
        allocator.allocate(request7);
    }

    private LinkedList<AllocationRequest> prepareRequests() {
        LinkedList<AllocationRequest> result = new LinkedList<>();
        AllocationRequest request1 = new AllocationRequest(130, "作业1");
        AllocationRequest request2 = new AllocationRequest(60, "作业2");
        AllocationRequest request3 = new AllocationRequest(100, "作业3");
        result.add(request1);
        result.add(request2);
        result.add(request3);
        return result;
    }

    private LinkedList<Partition> preparePartitions() {
        LinkedList<Partition> result = new LinkedList<>();
        Partition partition1 = new Partition(1, 600);
        partition1.setStartAddress(640);
        result.add(partition1);
        return result;
    }
}