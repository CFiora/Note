package com.fiora.note2.data.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
//        // union([ 2, 1, 3], [ 2, 3, 4]) ==  [1, 2, 3, 4]
//        System.out.println(union(Arrays.asList(2,1,3), Arrays.asList(2,3,4)));
        printInt();
        test2();
    }

    private static void test2() {
        String[] bedTypes = {"1", "2", "3"};
        int length = bedTypes.length;
        for (int i = 0; i < 10; i++) {
            int index = i%length;
            String bedType = bedTypes[index];
        }
    }

    private static int[][] a = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
    private static int row = 4;
    private static int col = 4;

    public static void printInt() {
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
            printIntPre(start, i, "i");
        }
        for(int j = start; j < endRow; j++) {
            printIntPre(j, endCol, "j");
        }
        for(int k = endCol; k > start; k--) {
            printIntPre(endRow, k, "k");
        }
        for(int m = endRow; m > start; m--) {
            printIntPre(m, start, "m");
        }
    }

    private static void printIntPre(int r, int c, String from) {
        System.out.print("from=="+from+"==r=="+r+"==c=="+c+"==");
        System.out.println(a[r][c]);
    }

    public static List<Integer> union(List<Integer> a, List<Integer> b) {
        List<Integer> result = new ArrayList<>(a); // 以集合a为基准
        for (Integer b1 : b) { // 遍历b集合，添加不属于a的元素
            boolean flag = false; // 假设不属于
            for (Integer a1 : a) {
                if (b1.intValue() == a1.intValue()) {
                    flag = true;    // 实际上b1属于a集合
                    break;
                }
            }
            if (!flag) {
                result.add(b1);
            }
            // 按照文字描述，已经完成。时间复杂度是O(n^2),空间复杂度为o(n)
            // 考虑到示例有排序，增加排序逻辑如下
            quickSort(result, 0, result.size() - 1);
        }
        return result;
    }

    // 快速排序 时间复杂度O(nlogn) 空间复杂度 O(n)
    public static void quickSort(List<Integer> list, int lIndex, int rIndex) {
        if (lIndex > rIndex) {
            return;
        }
        int ll = lIndex;
        int rr = rIndex;
        int key = list.get(ll);
        while (ll < rr) {
            while (ll < rr && list.get(rr) >= key) {
                rr--;
            }
            if (list.get(rr) <= key) {
                int temp = list.get(ll);
                list.set(ll, list.get(rr));
                list.set(rr, temp);
            }
            while ( ll < rr && list.get(ll) <= key) {
                ll++;
            }
            if (list.get(ll) >= key) {
                int temp = list.get(ll);
                list.set(ll, list.get(rr));
                list.set(rr, temp);
            }
        }
        if (ll > lIndex) {
            quickSort(list, lIndex, ll-1);
        }
        if (rr < rIndex) {
            quickSort(list, rr + 1, rIndex);
        }
    }


}
