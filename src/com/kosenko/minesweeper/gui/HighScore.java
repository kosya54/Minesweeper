package com.kosenko.minesweeper.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kosenko.minesweeper.controllers.GameController;
import com.google.gson.JsonElement;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.util.Vector;

public class HighScore {
    private JTable table;
    private GameController gameController;

    public HighScore(GameController gameController) {
        this.gameController = gameController;

        Vector<String> titles = new Vector<>();
        titles.add("Player");
        titles.add("Columns");
        titles.add("Rows");
        titles.add("Mines");

        Vector<Vector<String>> highScoreData = setData();

        table = new JTable(highScoreData, titles);
    }

    private Vector<Vector<String>> setData() {
        JsonArray jsonArrayHighScore = gameController.readHighScore();

        Vector<Vector<String>> highScoreData = new Vector<>();
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

    public JScrollPane getTable() {
        return new JScrollPane(table);
    }
}
