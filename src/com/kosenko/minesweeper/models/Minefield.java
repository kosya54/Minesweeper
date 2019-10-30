package com.kosenko.minesweeper.models;

import java.util.Random;

public class Minefield {
    private int width;
    private int height;
    private int countMines;

    private Cell cell;

    public Minefield(int width, int height, int countMines, Cell cell) {
        this.width = width;
        this.height = height;
        this.countMines = countMines;

        this.cell = cell;
    }

    private String[][] generateEmptyMinefield() {
        String[][] minefield = new String[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                minefield[i][j] = cell.getEmpty();
            }
        }
        return minefield;
    }

    private String[][] generateMinesInField() {
        Random random = new Random();
        String[][] minefield = generateEmptyMinefield();

        int count = 0;
        while (count != countMines) {
            int i = random.nextInt(height - 1);
            int j = random.nextInt(width - 1);

            if (!minefield[i][j].equals(cell.getBomb())) {
                minefield[i][j] = cell.getBomb();
                ++count;
            }
        }
        return minefield;
    }

    public String[][] generateMinefield() {
        String[][] minefield = generateMinesInField();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (minefield[i][j].equals(cell.getBomb())) {
                    continue;
                }

                int count = 0;

                int x = j + 1;
                if ((x >= 0 && x < width)) {
                    if (minefield[i][x].equals(cell.getBomb())) {
                        ++count;
                    }
                }

                x = j - 1;
                if (x < width && x >= 0) {
                    if (minefield[i][x].equals(cell.getBomb())) {
                        ++count;
                    }
                }

                int y = i + 1;
                if (y >= 0 && y < height) {
                    if (minefield[y][j].equals(cell.getBomb())) {
                        ++count;
                    }
                }

                y = i - 1;
                if (y < height && y >= 0) {
                    if (minefield[y][j].equals(cell.getBomb())) {
                        ++count;
                    }
                }

                if (count > 0) {
                    minefield[i][j] = cell.getNumbers()[count - 1];
                }
            }
        }
        return minefield;
    }
}