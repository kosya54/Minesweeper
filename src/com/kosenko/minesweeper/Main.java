package com.kosenko.minesweeper;

import com.kosenko.minesweeper.models.Cell;
import com.kosenko.minesweeper.models.Minefield;

import com.kosenko.minesweeper.gui.GUI;

public class Main {
    public static void main(String[] args) {
        int width = 9;
        int height = 9;
        int countMines = 10;

        Cell cell = new Cell();
        Minefield minefield = new Minefield(width, height, countMines, cell);

        String[][] array = minefield.generateMinefield();

        for (String[] arr : array) {
            for (String value : arr) {
                System.out.print(value);
            }
            System.out.println();
        }

        GUI gui = new GUI();
        gui.showGUI();
    }
}
