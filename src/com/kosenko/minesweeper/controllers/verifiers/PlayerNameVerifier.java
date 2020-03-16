package com.kosenko.minesweeper.controllers.verifiers;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PlayerNameVerifier extends InputVerifier {
    private final Border BORDER_RED = BorderFactory.createLineBorder(Color.RED);
    private final Border BORDER_GREEN = BorderFactory.createLineBorder(Color.GREEN);

    @Override
    public boolean verify(JComponent input) {
        JTextField playerName = (JTextField) input;
        if (playerName.getText().trim().equals("")) {
            playerName.setBorder(BORDER_RED);

            return false;
        }

        if (playerName.getText().length() < 3 || playerName.getText().length() > 10) {
            playerName.setBorder(BORDER_RED);

            return false;
        }

        playerName.setBorder(BORDER_GREEN);

        return true;
    }
}
