package org.jordon.os.constants;

public enum AllocationAlgorithm {
    FF("FF"),   // 首次适应 first fit
    NF("NF"),   // 循环首次适应 next fit
    BF("BF"),   // 最佳适应 Best Fit
    WF("WF"),   // 最坏适应 Worst Fit
    ;

    private String name;

    AllocationAlgorithm(String code) {
        this.name = name;
    }
}
