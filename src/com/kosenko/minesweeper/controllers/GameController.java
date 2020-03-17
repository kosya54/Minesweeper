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

//    private final static String SAVES_PATH = "src/com/kosenko/minesweeper/saves/";
    private final static String SAVES_PATH = "D:/Java/Minesweeper/src/com/kosenko/minesweeper/saves/";
    private final static String FILE_NAME = "HighScore";
    private final static String FILE_EXTENSION = ".sav";

    private File file;

    public GameController() {
        file = new File(SAVES_PATH, FILE_NAME + FILE_EXTENSION);
    }

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

    public static boolean isWon(JsonObject gameParameters, Cell[][] cells) {
        int totalCells = gameParameters.get("columns").getAsInt() * gameParameters.get("rows").getAsInt();
        int countOpened = GameController.getCountOpenedCells(cells);
        int mines = gameParameters.get("mines").getAsInt();

        return totalCells - countOpened == mines;
    }

    public static boolean isLose(int value) {
        return value == getMineValue();
    }

    public void writeHighScore(JsonObject gameSession) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            writer.write(gameSession.toString());
            writer.append(System.lineSeparator());
        }
    }

    public JsonArray readHighScore() {
        JsonArray highScoreArray = new JsonArray();
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                highScoreArray.add(new JsonParser().parse(line).getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScoreArray;
    }
}
