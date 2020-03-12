package com.kosenko.minesweeper.controllers;

import com.kosenko.minesweeper.models.Minefield;

public class GameSessionController {
    private final static int DEFAULT_GRID_LENGTH = 9;
    private final static int DEFAULT_COUNT_MINES = 10;
    private final static int MAX_GRID_LENGTH = 25;
    private final static int MAX_COUNT_MINES = 300;

    public static int getDefaultGridLength() {
        return DEFAULT_GRID_LENGTH;
    }

    public static int getDefaultCountMines() {
        return DEFAULT_COUNT_MINES;
    }

    public static int getMaxGridLength() {
        return MAX_GRID_LENGTH;
    }

    public static int getMaxCountMines() {
        return MAX_COUNT_MINES;
    }

    public static int getMineValue() {
        return Minefield.getMine();
    }

    public static boolean isCorrectLength(String playerName) {
        return playerName.length() > 3 && playerName.length() < 20;
    }

    public static boolean isCorrectGridLength(int gridLength) {
        return gridLength < DEFAULT_GRID_LENGTH || gridLength > MAX_GRID_LENGTH;
    }

    public static boolean isCorrectCountMines(int countMines) {
        return countMines >= DEFAULT_COUNT_MINES && countMines <= MAX_COUNT_MINES;
    }

    public static boolean isEmpty(String playerName) {
        return playerName.equals("");
    }

    public static boolean isNumber(String enteredNumber) {
        for (int i = 0; i < enteredNumber.length(); i++) {
            if (!Character.isDigit(enteredNumber.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static int getInt(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    public static int[][] getMineField(int columns, int rows, int countMines) {
        Minefield minefield = new Minefield(columns, rows, countMines);

        return minefield.generateMinefield();
    }
}

