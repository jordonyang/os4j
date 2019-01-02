package org.jordon.os;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void a() {
        int[][] a = new int[2000][2000];
        int[][] b = new int[2000][2000];

        long sa = System.currentTimeMillis();
        for(int i = 0; i < 2000; i++) {
            for(int j = 0; j < 2000; j++) {
                a[i][j] = 1;
            }
        }
        long ea = System.currentTimeMillis();
        long ua = ea - sa;
        System.out.println(ua);

        long sb = System.currentTimeMillis();
        for(int j = 0; j < 2000; j++) {
            for(int i = 0; i < 2000; i++) {
                b[i][j] = 1;
            }
        }
        long eb = System.currentTimeMillis();
        long ub = eb - sb;
        System.out.println(ub);
    }
}
