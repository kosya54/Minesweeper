package com.kosenko.minesweeper.controllers;

import com.kosenko.minesweeper.models.Cell;
import com.kosenko.minesweeper.models.Minefield;

public class MinefieldController {
    private final static int DEFAULT_WIDTH = 9;
    private final static int DEFAULT_HEIGHT = 9;
    private final static int DEFAULT_COUNT_MINES = 10;

    public int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    public int getDefaultCountMines() {
        return DEFAULT_COUNT_MINES;
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

/*    public static int[] getMineFieldParam(String width, String height, String countBomb) throws NumberFormatException {
        int[] param = new int[3];

        param[0] = Integer.parseInt(width);
        param[1] = Integer.parseInt(height);
        param[2] = Integer.parseInt(countBomb);

        return param;
    } */

    public static String[][] getMineField() {
        Cell cell = new Cell();
        Minefield minefield = new Minefield(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COUNT_MINES, cell);

        return minefield.generateMinefield();
    }
}

