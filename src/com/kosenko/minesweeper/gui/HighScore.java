package com.kosenko.minesweeper.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.kosenko.minesweeper.controllers.GameController;

import java.util.Vector;

public class HighScore {
    private JTable table;
    private GameController gameController;

    public HighScore(GameController gameController) {
        this.gameController = gameController;
    }

    public Vector<Vector<String>> getData() {
        Vector<Vector<String>> highScoreData = new Vector<>();

        JsonArray jsonArrayHighScore = gameController.readHighScore();
        for (JsonElement jsonElement : jsonArrayHighScore) {
            JsonObject playerJsonObject = jsonElement.getAsJsonObject();

            Vector<String> playerData = new Vector<>();

            playerData.add(playerJsonObject.get("playerName").getAsString());
            playerData.add(playerJsonObject.get("columns").getAsString());
            playerData.add(playerJsonObject.get("rows").getAsString());
            playerData.add(playerJsonObject.get("mines").getAsString());

            highScoreData.add(playerData);
        }
        return highScoreData;
    }

    private static Vector<String> getTitles() {
        Vector<String> titles = new Vector<>();

        titles.add("Player");
        titles.add("Columns");
        titles.add("Rows");
        titles.add("Mines");

        return titles;
    }

    public void refreshTable() {
        DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
        defaultTableModel.setDataVector(getData(), getTitles());
    }

    public JScrollPane getTable() {
        table = new JTable(getData(), getTitles());

        return new JScrollPane(table);
    }
}