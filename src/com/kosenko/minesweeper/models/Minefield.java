package com.kosenko.minesweeper.models;

import java.util.Random;

public class Minefield {
    private int columns;
    private int rows;
    private int countMines;
    private int bomb;

    public Minefield(int columns, int rows, int countMines) {
        this.columns = columns;
        this.rows = rows;
        this.countMines = countMines;
        bomb = -1;
    }

    private int[][] generateEmptyMinefield() {
        int[][] minefield = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = 0;
            }
        }
        return minefield;
    }

    private int[][] generateMinesInField() {
        Random random = new Random();
        int[][] minefield = generateEmptyMinefield();

        int count = 0;
        while (count != countMines) {
            int i = random.nextInt(rows - 1);
            int j = random.nextInt(columns - 1);

            if (minefield[i][j] != bomb) {
                minefield[i][j] = bomb;
                ++count;
            }
        }
        return minefield;
    }

    public int[][] generateMinefield() {
        int[][] minefield = generateMinesInField();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minefield[i][j]== bomb) {
                    continue;
                }

                int count = 0;
                int y;
                int x = j;

                //Север
                y = i - 1;
                if (y < rows && y >= 0) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Юг
                y = i + 1;
                if (y >= 0 && y < rows) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Запад
                x = j - 1;
                y = i;
                if (x < columns && x >= 0) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Восток
                x = j + 1;
                if ((x >= 0 && x < columns)) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Северо-восток
                x = j + 1;
                y = i - 1;
                if (y >= 0 && y < rows && x >= 0 && x < columns) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Юго-восток
                y = i + 1;
                if (y >= 0 && y < rows && x >= 0 && x < columns) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Северо-запад
                x = j - 1;
                y = i - 1;
                if (y < rows && y >= 0 && x < columns && x >= 0) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                //Юго-запад
                y = i + 1;
                if (y < rows && y >= 0 && x < columns && x >= 0) {
                    if (minefield[y][x] == bomb) {
                        ++count;
                    }
                }

                if (count > 0) {
                    minefield[i][j] = count;
                }
            }
        }
        return minefield;
    }
}