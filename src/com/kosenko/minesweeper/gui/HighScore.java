package com.kosenko.minesweeper.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kosenko.minesweeper.controllers.GameController;
import com.google.gson.JsonElement;

import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

public class HighScore {
    private Vector<String> titles;
    private Vector<Vector<String>> highScoreData;
    private JTable table;
    private GameController gameController;

    public HighScore(GameController gameController) {
        this.gameController = gameController;

        titles = new Vector<>();
        titles.add("Player");
        titles.add("Columns");
        titles.add("Rows");
        titles.add("Mines");

        highScoreData = setHighScoreData();

        table = new JTable(highScoreData, titles);
    }

    private Vector<Vector<String>> setHighScoreData() {
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
        System.out.println(highScoreData);

        return highScoreData;
    }

    public JPanel getHighScorePanel(int width, int height) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JPanel panel = new JPanel();
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
