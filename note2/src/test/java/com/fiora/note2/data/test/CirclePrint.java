package com.fiora.note2.data.test;

public class CirclePrint {
    private static int[][] a = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
    private static int row = 4;
    private static int col = 4;
    public static void main(String[] args) {
        int start = 0;
        while (col > start * 2 && row > start *2) {
            printCircle(start);
            start ++;
        }
    }
    private static void printCircle(int start) {
        int endCol = col - 1 - start;
        int endRow = row - 1 - start;
        for(int i = start; i < endCol; i++) {
            System.out.print(a[start][i] + " ");
        }
        for(int j = start; j < endRow; j++) {
            System.out.print(a[j][endCol] + " ");
        }
        for(int k = endCol; k > start; k--) {
            System.out.print(a[endRow][k] + " ");
        }
        for(int m = endRow; m > start; m--) {
            System.out.print(a[m][start] + " ");
        }
    }
}
