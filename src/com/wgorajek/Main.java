package com.wgorajek;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to for Advent of Code 2022");
        System.out.println();

        for (var day = 1; day <= 18 ; day++)
        {
            runSolution(day);
        }
//        runSolution(16);
}


    private static void runSolution(int day) throws Exception {
        System.out.printf("Day %02d loaded.\n", day);
        final Solution solution = Solution.getSolution(day);

        System.out.printf("The solution for Day %02d Part %d is: %s\n", day, 1, solution.getPart1Solution());
        System.out.printf("The solution for Day %02d Part %d is: %s\n", day, 2, solution.getPart2Solution());
    }
}
