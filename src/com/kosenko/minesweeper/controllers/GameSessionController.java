package com.kosenko.minesweeper.controllers;

import com.kosenko.minesweeper.models.Minefield;
import com.kosenko.minesweeper.gui.Cell;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

public class GameSessionController {
    private final static int DEFAULT_GRID_LENGTH = 9;
    private final static int DEFAULT_COUNT_MINES = 10;
    private final static int MAX_GRID_LENGTH = 25;
    private final static int MAX_COUNT_MINES = 300;
    
//    private final static String SAVES_PATH = "src/com/kosenko/minesweeper/saves/";
    private final static String SAVES_PATH = "D:/Java/Minesweeper/src/com/kosenko/minesweeper/saves/";
    private final static String FILE_EXTENSION = ".sav";

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

    private static int getCountOpenedCells(Cell[][] cells) {
        int count = 0;
        for (Cell[] innerArray : cells) {
            for (Cell cell : innerArray) {
                if (cell.isOpened()) {
                    ++count;
                }
            }
        }

        return count;
    }

    public static boolean isWon(JsonObject gameSessionParameters, Cell[][] cells) {
        int totalCells = gameSessionParameters.get("columns").getAsInt() * gameSessionParameters.get("rows").getAsInt();
        int countOpened = GameSessionController.getCountOpenedCells(cells);
        int mines = gameSessionParameters.get("mines").getAsInt();

        return totalCells - countOpened == mines;
    }

    public static boolean isLose(int value) {
        return value == getMineValue();
    }

    public static void writeHighscore(JsonObject gameSession) throws IOException {
        File file = new File(SAVES_PATH, "Highscore" + FILE_EXTENSION);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            writer.write(gameSession.toString());
        }
    }
}
