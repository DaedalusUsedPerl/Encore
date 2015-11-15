/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs130;

import java.util.BitSet;

/**
 *
 * @author Shubham
 */
public class P2Q2 {

    public static void main(String[] args) {
        for (int i = -10; i <= 10; i++) {
            for (int j = -10; j <= 10; j++) {
                test(i, j);
            }
        }
    }
    public static void test(int x, int y) {
        int result;
        System.out.println("\n" + x + " & " + y);
        if (x < y) {
            System.out.print("T");
            result = x - y;
        } else {
            System.out.print("F");
            result = y - x;
        }
        for (int i = 0; i < 2; i++) {
            if (result < (x + y) / 2) {
                System.out.print("T" + (i == 0 ? "" : "\n"));
                x += 2;
                y -= 1;
            } else {
                System.out.print("F" + (i == 0 ? "" : "\n"));
                x -= 2;
                y += 1;
            }
            result = result * 2;
        }
    }
}
