package org.jordon.os.lock;

import org.junit.Test;

public class BankerAlgorithmTest {

    @Test
    public void test() {
        int[] available = {3, 3, 2};
        int[][] max = {
                {7, 5, 3}, {3, 2, 2}, {9, 0, 2}, {2, 2, 2}, {4, 3, 3}
        };
        int[][] allocation = {
                {0, 1, 0}, {2, 0, 0}, {3, 0, 2}, {2, 1, 1}, {0, 0, 2}
        };
        int[][] need = {
                {7, 4, 3}, {1, 2, 2}, {6, 0, 0}, {0, 1, 1}, {4, 3, 1}
        };
        BankerAlgorithm banker = new BankerAlgorithm(available, max, allocation, need);
        System.out.println(banker);

        System.out.println("安全性分析：");
        System.out.println("存在安全序列？ " + banker.hasSecureSequence());
        System.out.println();

        System.out.println("P1请求资源：Request1(1,0,2)");
        int[] request1 = {1, 0, 2};
        System.out.println("存在安全序列？ " + banker.processRequest(1, request1));
        System.out.println();

        System.out.println("P4请求资源：Request4(3,3,0)");
        int[] request4 = {3, 3, 0};
        System.out.println("存在安全序列？ " + banker.processRequest(4, request4));
        System.out.println();

        System.out.println("P0请求资源：Request0(0,2,0)");
        int[] request0 = {0, 2, 0};
        System.out.println("存在安全序列？ " + banker.processRequest(0, request0));
        System.out.println();
    }
}