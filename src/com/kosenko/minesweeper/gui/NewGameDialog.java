package com.kosenko.minesweeper.gui;

import com.google.gson.JsonObject;

import com.kosenko.minesweeper.controllers.GameController;
import com.kosenko.minesweeper.controllers.verifiers.GridVerifier;
import com.kosenko.minesweeper.controllers.verifiers.PlayerNameVerifier;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

class NewGameDialog extends JDialog {
    private JTextField playerName;
    private JTextField columns;
    private JTextField rows;
    private JTextField mines;

    private JsonObject gameParameters;

    public NewGameDialog(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);

        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        gameParameters = new JsonObject();

        JLabel playerNameLabel = new JLabel("Введите ваше имя:", JLabel.LEFT);
        JLabel columnsLabel = new JLabel("Ширина поля:", JLabel.LEFT);
        JLabel rowsLabel = new JLabel("Высота поля:", JLabel.LEFT);
        JLabel minesLabel = new JLabel("Колличество мин на поле:", JLabel.LEFT);

        GridVerifier gridVerifier = new GridVerifier(GameController.getDefaultGridLength(),
                GameController.getMaxGridLength());
        GridVerifier minesVerifier = new GridVerifier(GameController.getDefaultCountMines(),
                (int) Math.pow(GameController.getDefaultGridLength(), 2) / 3);

        String defaultGridLength = Integer.toString(GameController.getDefaultGridLength());
        String defaultCountMines = Integer.toString(GameController.getDefaultCountMines());

        playerName = getPlayerNameField();
        columns = getGridField(gridVerifier, defaultGridLength);
        mines = getGridField(minesVerifier, defaultCountMines);
        rows = getGridField(gridVerifier, defaultGridLength);

        JButton ok = getButtonOk();
        JButton cancel = getButtonCancel();
        
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.registerKeyboardAction(e -> dispose(), escapeKey, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        rootPane.setDefaultButton(ok);

        GridBagConstraints constraints = getConstraints();

        add(playerNameLabel, constraints);
        add(playerName, constraints);

        add(columnsLabel, constraints);
        add(columns, constraints);

        add(rowsLabel, constraints);
        add(rows, constraints);

        add(minesLabel, constraints);
        add(mines, constraints);

        constraints.gridwidth = 1;
        add(ok, constraints);

        constraints.gridwidth = 1;
        constraints.gridx = 1;
        add(cancel, constraints);

        pack();
        setLocationRelativeTo(null);
    }

    private JButton getButtonOk() {
        JButton ok = new JButton("Ok");

        ok.setVerifyInputWhenFocusTarget(true);
        ok.addActionListener(e -> {
            if (!playerName.getInputVerifier().verify(playerName)) {
                JOptionPane.showMessageDialog(playerName.getParent(), "Имя должно быть от 3 до 10 символов");

                return;
            }

            gameParameters.addProperty("playerName", playerName.getText());

            if (!columns.getInputVerifier().verify(columns) || !rows.getInputVerifier().verify(rows)) {
                String message = String.format("Длина поля должна быть от %d до %d",
                        GameController.getDefaultGridLength(), GameController.getMaxGridLength());

                JOptionPane.showMessageDialog(columns.getParent(), message);

                return;
            }

            int columnsValue = Integer.parseInt(columns.getText());
            gameParameters.addProperty("columns", columnsValue);

            int rowsValue = Integer.parseInt(rows.getText());
            gameParameters.addProperty("rows", rowsValue);

            GridVerifier minesVerifier = (GridVerifier) mines.getInputVerifier();

            int maxMines = (columnsValue * rowsValue) / 3;
            minesVerifier.setMax(maxMines);

            if (!mines.getInputVerifier().verify(mines)) {
                String message = String.format("Колличество мин должно быть от %d до %d",
                        GameController.getDefaultCountMines(), maxMines);

                JOptionPane.showMessageDialog(mines.getParent(), message);

                return;
            }

            int minesValue = Integer.parseInt(mines.getText());
            gameParameters.addProperty("mines", minesValue);

            dispose();
        });

        return ok;
    }

    private JButton getButtonCancel() {
        JButton cancel = new JButton("Cancel");

        cancel.setVerifyInputWhenFocusTarget(false);
        cancel.addActionListener(e -> dispose());

        return cancel;
    }

    private JTextField getPlayerNameField() {
        JTextField playerName = new JTextField();

        playerName.setHorizontalAlignment(JTextField.CENTER);
        playerName.setInputVerifier(new PlayerNameVerifier());

        return playerName;
    }

    private JTextField getGridField(GridVerifier verifier, String defaultValue) {
        JTextField grid = new JTextField();

        grid.setText(defaultValue);
        grid.setInputVerifier(verifier);
        grid.setHorizontalAlignment(JTextField.CENTER);

        return grid;
    }

    private GridBagConstraints getConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;

        return constraints;
    }

    public JsonObject getGameParameters() {
        return gameParameters;
    }
}
