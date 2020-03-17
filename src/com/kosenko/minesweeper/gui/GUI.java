package com.kosenko.minesweeper.gui;

import com.google.gson.JsonObject;
import com.kosenko.minesweeper.controllers.GameController;
import com.kosenko.minesweeper.models.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;

public class GUI {
    private JFrame mainWindow;
    private JPanel cardContainer;
    private HighScore highScore;
    private CardLayout cardLayout;

    private GameController gameController;

    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;
    private final static int MENU_HEIGHT = 30;

    private int iconWidth;
    private int iconHeight;
    private int defaultWidth;
    private int defaultHeight;

    public GUI() {
        gameController = new GameController();
        highScore = new HighScore(gameController);

        iconWidth = Cell.getIconWidth();
        iconHeight = Cell.getIconHeight();

        defaultWidth = iconWidth * GameController.getDefaultGridLength();
        defaultHeight = iconHeight * GameController.getDefaultGridLength();

        cardLayout = new CardLayout();

        cardContainer = new JPanel();
        cardContainer.setLayout(cardLayout);
        cardContainer.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        cardContainer.add(getGreetingsPanel(), "greetings");
        cardContainer.add(highScore.getHighScorePanel(defaultWidth, defaultHeight), "highScore");

        mainWindow = new JFrame("Minesweeper");
        setDefaultFrameSize();

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

    private void resizeFrame(JsonObject gameSessionParameters) {
        mainWindow.setSize(iconWidth * gameSessionParameters.get("columns").getAsInt(),
                iconHeight * gameSessionParameters.get("rows").getAsInt()
                        + MENU_HEIGHT + ICON_H_GAP * gameSessionParameters.get("rows").getAsInt());
    }

    private void setDefaultFrameSize() {
        mainWindow.setSize(defaultWidth, defaultHeight + MENU_HEIGHT);
    }

    private JMenuBar getMainMenu() {
        JMenu file = new JMenu("File");

        JMenuItem newGame = new JMenuItem("New game");
        file.add(newGame);

        newGame.addActionListener(e -> {
            NewGameDialog newGameDialog = new NewGameDialog(mainWindow, "New game", true);
            newGameDialog.setVisible(true);

            JsonObject gameParameters = newGameDialog.getGameParameters();
            if (!gameParameters.has("playerName")
                    || !gameParameters.has("columns")
                    || !gameParameters.has("rows")
                    || !gameParameters.has("mines")) {
                cardLayout.show(cardContainer, "greetings");

                return;
            }

            JPanel minefield = getMinefield(gameParameters);
            resizeFrame(gameParameters);

            cardContainer.add(minefield, "minefield");
            cardLayout.show(cardContainer, "minefield");
        });

        JMenuItem highScore = new JMenuItem("High score");
        file.add(highScore);

        highScore.addActionListener(e -> {
            setDefaultFrameSize();
            cardLayout.show(cardContainer, "highScore");
        });

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
        JLabel greetings = new JLabel("Greetings panel", JLabel.CENTER);

        JPanel panel = new JPanel();
        panel.add(greetings);

        return panel;
    }

/*    private JPanel getHighScorePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("High score"));
//        panel.add(highScore);

        return panel;
    } */

    private JPanel getMinefield(JsonObject gameParameters) {
        int columns = gameParameters.get("columns").getAsInt();
        int rows = gameParameters.get("rows").getAsInt();
        int countMines = gameParameters.get("mines").getAsInt();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns, ICON_V_GAP, ICON_H_GAP));

        int[][] minefield = GameController.getMineField(columns, rows, countMines);
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

                            if (GameController.isLose(minefield[y][x])) {
                                cell.setMine();

                                JOptionPane.showMessageDialog(panel, "Game over!");

                                setDefaultFrameSize();
                                cardLayout.show(cardContainer, "highScore");
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

                        if (GameController.isWon(gameParameters, cells)) {
                            JOptionPane.showMessageDialog(panel, "Вы выиграли!");

                            try {
                                gameController.writeHighScore(gameParameters);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(panel, ex.getMessage());
                                ex.printStackTrace();
                            }

                            setDefaultFrameSize();
                            cardLayout.show(cardContainer, "highScore");
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

        if (minefield[y][x] == GameController.getMineValue()) {
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
            cells[y][x].setOpened();

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
        System.out.println("New game started!");
        for (int[] array : minefield) {
            System.out.println(Arrays.toString(array));
        }
    }
}
