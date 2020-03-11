package com.kosenko.minesweeper.gui;

import javax.swing.*;

public class Cell extends JButton {
    private final static int WIDTH = 40;
    private final static int HEIGHT = 40;
    private final static int COUNT_NUMBERS = 10;
    private final static String ICONS_PATH = "src/com/kosenko/minesweeper/resources/";
    //    private final static String ICONS_PATH = "D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/";
    private final static String FILE_EXTENSION = ".png";

    private Icon tile;
    private Icon rollOver;
    private Icon flag;
    private Icon mine;

    private Icon[] numbers;

    private int x;
    private int y;

    private boolean isOpened;
    private boolean isFlagged;

    public Cell() {
        tile = new ImageIcon(ICONS_PATH + "tile" + FILE_EXTENSION);
        rollOver = new ImageIcon(ICONS_PATH + "rollOverTile" + FILE_EXTENSION);
        flag = new ImageIcon(ICONS_PATH + "flag" + FILE_EXTENSION);
        mine = new ImageIcon(ICONS_PATH + "mine" + FILE_EXTENSION);

        Icon empty = new ImageIcon(ICONS_PATH + "empty" + FILE_EXTENSION);

        numbers = new Icon[COUNT_NUMBERS];
        numbers[0] = empty;

        for (int i = 1; i < COUNT_NUMBERS; i++) {
            numbers[i] = new ImageIcon(ICONS_PATH + "num" + i + FILE_EXTENSION);
        }

        isOpened = false;
        isFlagged = false;

        setParams();
    }

    private void setParams() {
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setRolloverIcon(rollOver);
        setSize(WIDTH, HEIGHT);
    }

    public int getPositionX() {
        return x;
    }

    public void setPositionX(int x) {
        this.x = x;
    }

    public int getPositionY() {
        return y;
    }

    public void setPositionY(int y) {
        this.y = y;
    }

    public void setTile() {
        setIcon(tile);
        setRolloverIcon(rollOver);
    }

    public void setMine() {
        setEnabled(false);
        setDisabledIcon(mine);
    }

    public void setOpened() {
        isOpened = true;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setFlag() {
        setIcon(flag);
        setRolloverIcon(flag);
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setNumber(int index) {
        setEnabled(false);
        setDisabledIcon(numbers[index]);
    }
}