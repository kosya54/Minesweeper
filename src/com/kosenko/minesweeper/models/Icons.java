package com.kosenko.minesweeper.models;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Icons {
    private final String PATH = "com/kosenko/minesweeper/resources/";
    private Image bomb;
    private List<ImageIcon> numbers;

    public Icons() {
        bomb = getBombImage();
    }

    private Image getBombImage() {
        return new ImageIcon(PATH + "bomb.png").getImage();
    }
}
