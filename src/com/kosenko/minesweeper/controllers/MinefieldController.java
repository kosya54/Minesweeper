package com.kosenko.minesweeper.controllers;

import com.kosenko.minesweeper.models.Minefield;

public class MinefieldController {
    private final static int DEFAULT_COLUMNS = 9;
    private final static int DEFAULT_ROWS = 9;
    private final static int DEFAULT_COUNT_MINES = 10;

    public static int getDefaultColumns() {
        return DEFAULT_COLUMNS;
    }

    public static int getDefaultRows() {
        return DEFAULT_ROWS;
    }

    public static int getDefaultCountMines() {
        return DEFAULT_COUNT_MINES;
    }

    public static int getMineValue() {
        return Minefield.getMine();
    }

    public static boolean isNumber(String enteredNumber) {
        for (int i = 0; i < enteredNumber.length(); i++) {
            if (!Character.isDigit(enteredNumber.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static int getInt(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    public static int[][] getMineField(int columns, int rows, int countMines) {
        Minefield minefield = new Minefield(columns, rows, countMines);

        return minefield.generateMinefield();
    }
}

