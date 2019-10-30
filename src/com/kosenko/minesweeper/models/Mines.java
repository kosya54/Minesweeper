package com.kosenko.minesweeper.models;

import java.util.Random;

public class Mines {
    private int width;
    private int height;
    private int countMines;

    private static final String ANSI_COLOR = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public Mines(int width, int height, int countMines) {
        this.width = width;
        this.height = height;
        this.countMines = countMines;
    }

    public int[] generateMines() {
        int[] minefield = new int[width * height];

        Random random = new Random();

        int count = 0;
        while (count != countMines) {
            int randomIndex = random.nextInt(minefield.length - 1);
            if (minefield[randomIndex] != 1) {
                minefield[randomIndex] = 1;
                ++count;
            }
        }

        return minefield;
    }

    public void printMines(int[] array) {
        int i = 0;
        int j = 0;
        while (i < array.length) {
            if (j == width) {
                System.out.println();
                j = 0;
            }

            if (array[i] == 1) {
                System.out.print(ANSI_COLOR + array[i] + ANSI_RESET + " ");
            } else {
                System.out.print(array[i] + " ");
            }
            ++i;
            ++j;
        }

        int count = 0;
        for (int number : array) {
            if (number == 1) {
                ++count;
            }
        }

        System.out.printf("%n%nMines count: %d%n%n", count);
    }

    public boolean testPrintMines(int[] array) {
        int count = 0;
        for (int number : array) {
            if (number == 1) {
                ++count;
            }
        }

        return count >= countMines && count <= countMines;
    }
}