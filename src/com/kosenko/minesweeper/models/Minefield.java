package com.kosenko.minesweeper.models;

import java.util.Random;

public class Minefield {
    private final static int MINE = -1;

    private int columns;
    private int rows;
    private int countMines;

    public Minefield(int columns, int rows, int countMines) {
        this.columns = columns;
        this.rows = rows;
        this.countMines = countMines;
    }

    public static int getMine() {
        return MINE;
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

            if (minefield[i][j] != MINE) {
                minefield[i][j] = MINE;
                ++count;
            }
        }
        return minefield;
    }

    public int[][] generateMinefield() {
        int[][] minefield = generateMinesInField();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minefield[i][j]== MINE) {
                    continue;
                }

                int count = 0;
                int y;
                int x = j;

                //Север
                y = i - 1;
                if (y < rows && y >= 0) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Юг
                y = i + 1;
                if (y >= 0 && y < rows) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Запад
                x = j - 1;
                y = i;
                if (x < columns && x >= 0) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Восток
                x = j + 1;
                if ((x >= 0 && x < columns)) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Северо-восток
                x = j + 1;
                y = i - 1;
                if (y >= 0 && y < rows && x >= 0 && x < columns) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Юго-восток
                y = i + 1;
                if (y >= 0 && y < rows && x >= 0 && x < columns) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Северо-запад
                x = j - 1;
                y = i - 1;
                if (y < rows && y >= 0 && x < columns && x >= 0) {
                    if (minefield[y][x] == MINE) {
                        ++count;
                    }
                }

                //Юго-запад
                y = i + 1;
                if (y < rows && y >= 0 && x < columns && x >= 0) {
                    if (minefield[y][x] == MINE) {
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