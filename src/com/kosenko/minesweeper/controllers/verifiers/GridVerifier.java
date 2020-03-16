package com.kosenko.minesweeper.controllers.verifiers;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridVerifier extends InputVerifier {
    private final Border BORDER_RED = BorderFactory.createLineBorder(Color.RED);
    private final Border BORDER_GREEN = BorderFactory.createLineBorder(Color.GREEN);

    private int min;
    private int max;

    public GridVerifier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField grid = (JTextField) input;
        if (grid.getText().trim().equals("")) {
            grid.setBorder(BORDER_RED);

            return false;
        }

        if (!isNumber(grid.getText())) {
            grid.setBorder(BORDER_RED);

            return false;
        }

        int gridLength = Integer.parseInt(grid.getText());
        if (gridLength < min || gridLength > max) {
            grid.setBorder(BORDER_RED);

            return false;
        }

        grid.setBorder(BORDER_GREEN);

        return true;
    }

    private static boolean isNumber(String enteredNumber) {
        for (int i = 0; i < enteredNumber.length(); i++) {
            if (!Character.isDigit(enteredNumber.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}

