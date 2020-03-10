package com.kosenko.minesweeper.gui;

import com.kosenko.minesweeper.controllers.MinefieldController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GUI {
    private JFrame mainWindow;
    private JPanel cardContainer;
    private CardLayout cardLayout;

//    private MinefieldController minefieldController;

    private final static int ICON_WIDTH = 40;
    private final static int ICON_HEIGHT = 40;
    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;
    private final static int MENU_HEIGHT = 30;

    private int defaultWidth;
    private int defaultHeight;

    public GUI() {
//        minefieldController = new MinefieldController();
        defaultWidth = ICON_WIDTH * MinefieldController.getDefaultColumns();
        defaultHeight = ICON_HEIGHT * MinefieldController.getDefaultRows();

        cardLayout = new CardLayout();

        cardContainer = new JPanel();
        cardContainer.setLayout(cardLayout);
        cardContainer.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        cardContainer.add(getGreetingsPanel(), "greetings");
        cardContainer.add(getHighScorePanel(), "highScore");

        mainWindow = new JFrame("Minesweeper");
        mainWindow.setSize(defaultWidth, defaultHeight + MENU_HEIGHT);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setJMenuBar(getMainMenu());
        mainWindow.add(cardContainer);
    }

    public void showGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Error");
            }

            mainWindow.setVisible(true);
            mainWindow.setLocationRelativeTo(null);
        });
    }

    private JMenuBar getMainMenu() {
        JMenu file = new JMenu("File");

        JMenuItem newGame = new JMenuItem("New game");
        file.add(newGame);

        newGame.addActionListener(e -> {
            NewGameDialog newGameDialog = new NewGameDialog(mainWindow, "New game", true);
            newGameDialog.setVisible(true);

            int[] param = newGameDialog.getDialogData();

            JPanel minefield = getMinefield(param[0], param[1], param[2]);
            mainWindow.setSize((ICON_WIDTH * param[0]), (ICON_HEIGHT * param[1]) + MENU_HEIGHT);

            cardContainer.add(minefield, "minefield");
            cardLayout.show(cardContainer,"minefield");
        });

        JMenuItem highScore = new JMenuItem("High score");
        file.add(highScore);

        JMenuItem about = new JMenuItem("About");
        file.add(about);

        file.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        exit.addActionListener(e -> System.exit(0));

        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(defaultWidth, MENU_HEIGHT));
        menuBar.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, Color.LIGHT_GRAY));
        menuBar.add(file);

        return menuBar;
    }

    private JPanel getGreetingsPanel() {
        return new JPanel();
    }

    private JPanel getHighScorePanel() {
        return new JPanel();
    }

    private JPanel getMinefield(int columns, int rows, int countMines) {
        GridLayout layout = new GridLayout(rows, columns, ICON_H_GAP, ICON_V_GAP);

        JPanel panel = new JPanel();
        panel.setLayout(layout);

        int[][] minefield = MinefieldController.getMineField(columns, rows, countMines);
        temporaryShowMinefieldArray(minefield);

        Cell[][] cells = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = new Cell();
                cell.setTile();
                cell.setPositionX(j);
                cell.setPositionY(i);

                cell.addActionListener(e -> {
//                    System.out.println("X: " + cell.getPositionX() + " Y: " + cell.getPositionY());

                    if (minefield[cell.getPositionY()][cell.getPositionX()] == MinefieldController.getMineValue()) {
                        cell.setMine();

                        JOptionPane.showMessageDialog(panel, "Game over!");
                        cardLayout.show(cardContainer,"highScore");
                    }

                    if (minefield[cell.getPositionY()][cell.getPositionX()] != 0
                            && minefield[cell.getPositionY()][cell.getPositionX()] != MinefieldController.getMineValue()) {
                        cell.setNumber(minefield[cell.getPositionY()][cell.getPositionX()]);

                        return;
                    }

                    openCells(cells, minefield, cell.getPositionX(), cell.getPositionY());
                });

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                            cell.setFlag();
                        }
                    }
                });

                cells[i][j] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    private void openCells(Cell[][] cells, int[][] minefield, int x, int y) {
        showLeftCells(cells, minefield, x, y);
        showTopCells(cells, minefield, x, y);
        showRightCells(cells, minefield, x, y);
    }

    private void showLeftCells(Cell[][] cells, int[][] minefield, int x, int y) {
        int j = x;
        while (j >= 0) {
            if (minefield[y][j] > 0) {
                continue;
            }

            showEmptyCell(cells, minefield, j, y);
            --j;
        }
    }

    private void showTopCells(Cell[][] cells, int[][] minefield, int x, int y) {
        int i = y;
        while (i >= 0) {
            if (minefield[i][x] > 0) {
                continue;
            }

            showEmptyCell(cells, minefield, x, i);
            --i;
        }
    }

    private void showRightCells(Cell[][] cells, int[][] minefield, int x, int y) {
        int j = x;
        while (j < minefield[y].length) {
            if (minefield[y][j] > 0) {
                continue;
            }

            showEmptyCell(cells, minefield, j, y);
            ++j;
        }
    }

    private void showEmptyCell(Cell[][] cells, int[][] minefield, int x, int y) {
        for (int i = y - 1; i <= y + 1; i++) {
            if (i >= minefield.length || i < 0) {
                continue;
            }

            for (int j = x - 1; j <= x + 1; j++) {
                if (j >= minefield[i].length || j < 0) {
                    continue;
                }

                if (minefield[i][j] >= 0) {
                    cells[i][j].setNumber(minefield[i][j]);
//                    break;
                }
            }
        }
    }

    private void temporaryShowMinefieldArray(int[][] minefield) {
        for (int[] array : minefield) {
            System.out.println(Arrays.toString(array));
        }
    }

    private static class NewGameDialog extends JDialog implements ActionListener {
        private JTextField width;
        private JTextField height;
        private JTextField countBomb;
        private int[] dialogData;

        private final static int FIELD_SIZE = 20;

        private JButton ok;
        private JButton cancel;

        public NewGameDialog(JFrame parent, String name, boolean modal) {
            super(parent, name, modal);

            width = new JTextField(FIELD_SIZE);
            height = new JTextField(FIELD_SIZE);
            countBomb = new JTextField(FIELD_SIZE);

            dialogData = new int[3];

            ok = new JButton("ok");
            ok.addActionListener(this);

            cancel = new JButton("cancel");
            cancel.addActionListener(this);

            add(width);
            add(height);
            add(countBomb);
            add(ok);
            add(cancel);

            GridLayout layout = new GridLayout(5, 1, ICON_H_GAP, ICON_V_GAP);

            setLayout(layout);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(200, 200);
            setLocationRelativeTo(null);
        }

        public int[] getDialogData() {
            return dialogData;
        }

        private void setDialogData(int index, String value) {
            if (value.equals("")) {
                dialogData[index] = MinefieldController.getDefaultColumns();
                System.out.println(dialogData[index]);
            } else if (MinefieldController.isNumber(value)) {
                dialogData[index] = MinefieldController.getInt(value);
                System.out.println(dialogData[index]);
            } else {
                JOptionPane.showMessageDialog(this, "Введите число.");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                setDialogData(0, width.getText());
                setDialogData(1, height.getText());
                setDialogData(2, countBomb.getText());

                dispose();
            }

            if (e.getSource() == cancel) {
                dispose();
            }
        }
    }
}

