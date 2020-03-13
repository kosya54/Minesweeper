package com.kosenko.minesweeper.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kosenko.minesweeper.models.Minefield;
import com.kosenko.minesweeper.models.Cell;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;

public class GameController {
    private final static int DEFAULT_GRID_LENGTH = 9;
    private final static int DEFAULT_COUNT_MINES = 10;
    private final static int MAX_GRID_LENGTH = 25;

    private final static String SAVES_PATH = "src/com/kosenko/minesweeper/saves/";
//    private final static String SAVES_PATH = "D:/Java/Minesweeper/src/com/kosenko/minesweeper/saves/";
    private final static String FILE_NAME = "HighScore";
    private final static String FILE_EXTENSION = ".sav";

    private final static File FILE = new File(SAVES_PATH, FILE_NAME + FILE_EXTENSION);

    public static int getDefaultGridLength() {
        return DEFAULT_GRID_LENGTH;
    }

    public static int getDefaultCountMines() {
        return DEFAULT_COUNT_MINES;
    }

    public static int getMaxGridLength() {
        return MAX_GRID_LENGTH;
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
        int countOpened = GameController.getCountOpenedCells(cells);
        int mines = gameSessionParameters.get("mines").getAsInt();

        return totalCells - countOpened == mines;
    }

    public static boolean isLose(int value) {
        return value == getMineValue();
    }

//TODO: Переделать на не статик методы, запускать из actionListenera меню

    public static void writeHighScore(JsonObject gameSession) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE, true), StandardCharsets.UTF_8))) {
            writer.write(gameSession.toString());
            writer.append(System.lineSeparator());
        }
    }

    public static JsonArray readHighScore() throws IOException {
        JsonArray highScoreArray = new JsonArray();
        try (FileReader fileReader = new FileReader(FILE); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                highScoreArray.add(new JsonParser().parse(line).getAsJsonObject());
            }
        }

        return highScoreArray;
    }
}