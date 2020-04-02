package com.kosenko.minesweeper.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import com.kosenko.minesweeper.models.Minefield;
import com.kosenko.minesweeper.models.Cell;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GameController {
    private int[][] minefield;

    private JsonObject gameParameters;
    private boolean isFirstClick;
    private File file;
    
    private int columns;
    private int rows;
    private int countMines;

    private final static int DEFAULT_GRID_LENGTH = 9;
    private final static int DEFAULT_COUNT_MINES = 10;
    private final static int MAX_GRID_LENGTH = 25;

//    private final static String SAVES_PATH = "D:/Java/Minesweeper/src/com/kosenko/minesweeper/saves/";
    private final static String SAVES_PATH = "src/com/kosenko/minesweeper/saves/";
    private final static String FILE_NAME = "HighScore";
    private final static String FILE_EXTENSION = ".sav";

    public GameController() {
        file = new File(SAVES_PATH, FILE_NAME + FILE_EXTENSION);
    }

    public boolean isFirstClick() {
        return isFirstClick;
    }

    public void setFirstClick(boolean isFirstClick) {
        this.isFirstClick = isFirstClick;
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

    public void startGame(JsonObject gameParameters, int firstClickX, int firstClickY) {
        this.gameParameters = gameParameters;

        columns = gameParameters.get("columns").getAsInt();
        rows = gameParameters.get("rows").getAsInt();
        countMines = gameParameters.get("mines").getAsInt();

        Minefield minefield = new Minefield(columns, rows, countMines, firstClickX, firstClickY);
        this.minefield = minefield.generateMinefield();

        temporaryPrintMinefield();
    }

    //-1 игра проиграна, 1 игра выиграна
    public int gameOver(int x, int y, Cell[][] cells) {
        if (isLose(minefield[y][x])) {
            cells[y][x].setMine();

            return -1;
        }

        if (isWon(cells)) {
            try {
                writeHighScore(gameParameters);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return 1;
        }

        reveal(cells, x, y);

        return 0;
    }

    private boolean outOfRange(int x, int y) {
        return x < 0 || y < 0 || x >= columns || y >= rows;
    }

    private void reveal(Cell[][] cells, int x, int y) {
        if (outOfRange(x, y)) {
            return;
        }

        if (minefield[y][x] == GameController.getMineValue()) {
            return;
        }

        if (cells[y][x].isOpened()) {
            return;
        }

        if (cells[y][x].isFlagged()) {
            return;
        }

        if (minefield[y][x] > 0) {
            cells[y][x].setNumber(minefield[y][x]);
            cells[y][x].setOpened();

            return;
        }

        cells[y][x].setOpened();
        cells[y][x].setNumber(minefield[y][x]);

        reveal(cells, x - 1, y - 1);
        reveal(cells, x, y - 1);
        reveal(cells, x + 1, y - 1);
        reveal(cells, x - 1, y);
        reveal(cells, x + 1, y);
        reveal(cells, x - 1, y + 1);
        reveal(cells, x, y + 1);
        reveal(cells, x + 1, y + 1);
    }

    private static int getOpenedCellsCount(Cell[][] cells) {
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

    private boolean isWon(Cell[][] cells) {
        int totalCells = columns * rows;
        int countOpened = GameController.getOpenedCellsCount(cells);

        return totalCells - countOpened == countMines;
    }

    private static boolean isLose(int value) {
        return value == getMineValue();
    }

    private void writeHighScore(JsonObject gameParameters) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            writer.write(gameParameters.toString());
            writer.append(System.lineSeparator());
        }
    }

    public JsonArray readHighScore() {
        JsonArray highScoreArray = new JsonArray();

        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    highScoreArray.add(new JsonParser().parse(line).getAsJsonObject());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return highScoreArray;
    }

    private void temporaryPrintMinefield() {
        System.out.println("New Game started:");
        for (int[] rowArray : minefield) {
            System.out.println(Arrays.toString(rowArray));
        }
        System.out.println();
    }
}
