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

    public static String[][] getMineField() {
        Cell cell = new Cell();
        Minefield minefield = new Minefield(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COUNT_MINES, cell);

        return minefield.generateMinefield();
    }
}
