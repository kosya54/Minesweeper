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

    private final static int ICON_WIDTH = 40;
    private final static int ICON_HEIGHT = 40;
    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;
    private final static int MENU_HEIGHT = 30;

    private int defaultWidth;
    private int defaultHeight;

    public GUI() {
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

            int[] minefieldParameters = newGameDialog.getMinefieldParameters();

            JPanel minefield = getMinefield(minefieldParameters[0], minefieldParameters[1], minefieldParameters[2]);
            mainWindow.setSize((ICON_WIDTH * minefieldParameters[0]), (ICON_HEIGHT * minefieldParameters[1]) + MENU_HEIGHT);

            cardContainer.add(minefield, "minefield");
            cardLayout.show(cardContainer, "minefield");
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

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (cell.isFlagged()) {
                                cell.setFlagged(false);
                            }

                            int x = cell.getPositionX();
                            int y = cell.getPositionY();

                            if (minefield[y][x] == MinefieldController.getMineValue()) {
                                cell.setMine();

                                JOptionPane.showMessageDialog(panel, "Game over!");
                                cardLayout.show(cardContainer, "highScore");

                                //TODO: запись high score в файл
                            }

                            reveal(cells, minefield, x, y, rows, columns);
                        }

                        if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                            if (!cell.isFlagged()) {
                                cell.setFlagged(true);
                                cell.setFlag();
                            } else {
                                cell.setFlagged(false);
                                cell.setTile();
                            }
                        }
                    }
                });

                cells[i][j] = cell;
                panel.add(cell);
            }
        }

        return panel;
    }

    private boolean outOfRange(int x, int y, int rows, int columns) {
        return x < 0 || y < 0 || x >= columns || y >= rows;
    }

    private void reveal(Cell[][] cells, int[][] minefield, int x, int y, int rows, int columns) {
        if (outOfRange(x, y, rows, columns)) {
            return;
        }

        if (minefield[y][x] == MinefieldController.getMineValue()) {
            return;
        }

        if (cells[y][x].isOpened()) {
            return;
        }

        if (cells[y][x].isFlagged()) {
            return;
        }

        if (minefield[y][x] > 0) {
            cells[y][x].setNumber(minefield[y][x]);

            return;
        }

        cells[y][x].setOpened();
        cells[y][x].setNumber(minefield[y][x]);

        reveal(cells, minefield, x - 1, y - 1, rows, columns);
        reveal(cells, minefield, x, y - 1, rows, columns);
        reveal(cells, minefield, x + 1, y - 1, rows, columns);
        reveal(cells, minefield, x - 1, y, rows, columns);
        reveal(cells, minefield, x + 1, y, rows, columns);
        reveal(cells, minefield, x - 1, y + 1, rows, columns);
        reveal(cells, minefield, x, y + 1, rows, columns);
        reveal(cells, minefield, x + 1, y + 1, rows, columns);
    }

    private void temporaryShowMinefieldArray(int[][] minefield) {
        for (int[] array : minefield) {
            System.out.println(Arrays.toString(array));
        }
    }

    private static class NewGameDialog extends JDialog implements ActionListener {
        private JTextField columns;
        private JTextField rows;
        private JTextField countMine;

        private JButton ok;
        private JButton cancel;

        private int[] minefieldParameters;

        private final static int FIELD_SIZE = 20;

        public NewGameDialog(JFrame parent, String name, boolean modal) {
            super(parent, name, modal);

            columns = new JTextField(FIELD_SIZE);
            rows = new JTextField(FIELD_SIZE);
            countMine = new JTextField(FIELD_SIZE);

            minefieldParameters = new int[3];

            ok = new JButton("ok");
            ok.addActionListener(this);

            cancel = new JButton("cancel");
            cancel.addActionListener(this);

            add(columns);
            add(rows);
            add(countMine);
            add(ok);
            add(cancel);

            GridLayout layout = new GridLayout(5, 1, ICON_H_GAP, ICON_V_GAP);

            setLayout(layout);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(200, 200);
            setLocationRelativeTo(null);
        }

        public int[] getMinefieldParameters() {
            return minefieldParameters;
        }

        private void setMinefieldParameters(int index, String value) {
            if (value.equals("")) {
                minefieldParameters[index] = MinefieldController.getDefaultColumns();
            } else if (MinefieldController.isNumber(value)) {
                minefieldParameters[index] = MinefieldController.getInt(value);
            } else {
                JOptionPane.showMessageDialog(this, "Введите число.");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                setMinefieldParameters(0, columns.getText());
                setMinefieldParameters(1, rows.getText());
                setMinefieldParameters(2, countMine.getText());

                dispose();
            }

            if (e.getSource() == cancel) {
                dispose();
            }
        }
    }
}
