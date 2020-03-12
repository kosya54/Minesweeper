package com.kosenko.minesweeper.gui;

import com.google.gson.JsonObject;
import com.kosenko.minesweeper.controllers.GameSessionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class NewGameDialog extends JDialog implements ActionListener {
    private JTextField playerName;
    private JTextField columns;
    private JTextField rows;
    private JTextField mines;

    private JButton ok;
    private JButton cancel;

    private JsonObject gameSessionParameters;

    public NewGameDialog(JFrame parent, String name, boolean modal) {
        super(parent, name, modal);

        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Введите ваше имя:", JLabel.LEFT);
        JLabel columnsLabel = new JLabel("Укажите ширину поля:", JLabel.LEFT);
        JLabel rowsLabel = new JLabel("Укажите высоту поля:", JLabel.LEFT);
        JLabel minesLabel = new JLabel("Укажите колличество мин на поле:", JLabel.LEFT);

        playerName = new JTextField();
        columns = new JTextField();
        rows = new JTextField();
        mines = new JTextField();

        gameSessionParameters = new JsonObject();

        ok = new JButton("ok");
        ok.addActionListener(this);

        cancel = new JButton("cancel");
        cancel.addActionListener(this);

        GridBagConstraints constraints = getConstraints();

        add(nameLabel, constraints);
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

    public JsonObject getGameSessionParameters() {
        return gameSessionParameters;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            if (GameSessionController.isEmpty(playerName.getText())) {
                JOptionPane.showMessageDialog(this, "Укажите имя.");

                return;
            }

            if (!GameSessionController.isCorrectLength(playerName.getText())) {
                JOptionPane.showMessageDialog(this, "Имя должно быть > 3 и < 20 символов.");

                return;
            }

            gameSessionParameters.addProperty("playerName", playerName.getText());

            if (GameSessionController.isEmpty(columns.getText())) {
                gameSessionParameters.addProperty("columns", GameSessionController.getDefaultGridLength());
            } else {
                if (GameSessionController.isNumber(columns.getText())) {
                    JOptionPane.showMessageDialog(this, "Введите число.");

                    return;
                }

                int columnsValue = GameSessionController.getInt(columns.getText());
                if (GameSessionController.isCorrectGridLength(columnsValue)) {
                    String message = String.format("Длина поля должна быть > %d и < %d",
                            GameSessionController.getDefaultGridLength(), GameSessionController.getMaxGridLength());

                    JOptionPane.showMessageDialog(this, message);

                    return;
                }

                gameSessionParameters.addProperty("columns", columnsValue);
            }

            if (GameSessionController.isEmpty(rows.getText())) {
                gameSessionParameters.addProperty("rows", GameSessionController.getDefaultGridLength());
            } else {
                if (GameSessionController.isNumber(rows.getText())) {
                    JOptionPane.showMessageDialog(this, "Введите число.");

                    return;
                }

                int rowsValue = GameSessionController.getInt(rows.getText());
                if (GameSessionController.isCorrectGridLength(rowsValue)) {
                    String message = String.format("Длина поля должна быть > %d и < %d",
                            GameSessionController.getDefaultGridLength(), GameSessionController.getMaxGridLength());

                    JOptionPane.showMessageDialog(this, message);

                    return;
                }

                gameSessionParameters.addProperty("rows", rowsValue);
            }

            if (GameSessionController.isEmpty(mines.getText())) {
                gameSessionParameters.addProperty("mines", GameSessionController.getDefaultCountMines());
            } else {
                if (GameSessionController.isNumber(mines.getText())) {
                    JOptionPane.showMessageDialog(this, "Введите число.");

                    return;
                }

                int minesValue = GameSessionController.getInt(mines.getText());
                if (!GameSessionController.isCorrectCountMines(minesValue)) {
                    String message = String.format("Колличество мин должно быть > %d и < %d",
                            GameSessionController.getDefaultCountMines(), GameSessionController.getMaxCountMines());

                    JOptionPane.showMessageDialog(this, message);

                    return;
                }

                gameSessionParameters.addProperty("mines", minesValue);
            }

            dispose();
        }

        if (e.getSource() == cancel) {
            dispose();
        }
    }
}
