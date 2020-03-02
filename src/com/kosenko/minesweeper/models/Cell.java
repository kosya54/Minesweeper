package com.kosenko.minesweeper.models;

public class Cell {
    private String bomb;
    private String empty;
    private String[] numbers;

    public Cell() {
        bomb = "[*] ";
        empty = "[ ] ";

        numbers = new String[9];
        for (int i = 0, j = 1; i < 9; i++, j++) {
            numbers[i] = "[" + j + "] ";
        }
    }

    public String getBomb() {
        return bomb;
    }

    public String getEmpty() {
        return empty;
    }

    public String[] getNumbers() {
        return numbers;
    }
}
