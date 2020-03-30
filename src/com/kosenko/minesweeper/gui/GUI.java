package com.kosenko.minesweeper.gui;

import com.google.gson.JsonObject;
import com.kosenko.minesweeper.controllers.GameController;
import com.kosenko.minesweeper.models.Cell;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GUI {
    private JFrame mainFrame;
    private JPanel panelContainer;
    private CardLayout cardLayout;

    private HighScore highScore;
    private GameController gameController;

    private final static int ICON_V_GAP = 1;
    private final static int ICON_H_GAP = 1;
    private final static int MENU_HEIGHT = 30;

    private final Border PANELS_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY);

    private int iconWidth;
    private int iconHeight;
    private int width;
    private int height;

    public GUI() {
        GridBagConstraints gridBagConstraints = getGridBagConstraints();

        gameController = new GameController();
        highScore = new HighScore(gameController);

        iconWidth = Cell.getIconWidth();
        iconHeight = Cell.getIconHeight();

        width = iconWidth * GameController.getDefaultGridLength();
        height = iconHeight * GameController.getDefaultGridLength();

        panelContainer = getPanelContainer();

        mainFrame = getMainFrame();
        mainFrame.setJMenuBar(getMenuBar());
        mainFrame.add(panelContainer, gridBagConstraints);
    }

    public void showGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Error");
            }

            mainFrame.setVisible(true);
            mainFrame.setLocationRelativeTo(null);
        });
    }

    private GridBagConstraints getGridBagConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 3, 3, 3);
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

    private void resizeMainFrameToGrid(JsonObject gameParameters) {
        int columns = gameParameters.get("columns").getAsInt();
        int rows = gameParameters.get("rows").getAsInt();

        mainFrame.setSize(iconWidth * columns + ICON_V_GAP * columns,
                (iconHeight * rows) + MENU_HEIGHT + ICON_H_GAP * rows);
    }

    private JFrame getMainFrame() {
        JFrame frame = new JFrame("Minesweeper");
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return frame;
    }

    private JPanel getPanelContainer() {
        cardLayout = new CardLayout();

        JPanel panel = new JPanel(cardLayout);
        panel.setBorder(PANELS_BORDER);
        panel.add(getGreetingsPanel(), "greetingsPanel");
        panel.add(highScore.getTable(), "highScorePanel");

        return panel;
    }

    private JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getMenu());
        menuBar.setPreferredSize(new Dimension(width, MENU_HEIGHT));

        return menuBar;
    }

    private JMenu getMenu() {
        JMenu menu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            NewGameDialog newGameDialog = new NewGameDialog(mainFrame, "New Game", true);
            newGameDialog.setVisible(true);

            JsonObject gameParameters = newGameDialog.getGameParameters();
            if (!gameParameters.has("playerName")
                    || !gameParameters.has("columns")
                    || !gameParameters.has("rows")
                    || !gameParameters.has("mines")) {
                cardLayout.show(panelContainer, "greetings");

                return;
            }

            JPanel minefieldPanel = getMinefieldPanel(gameParameters);
            resizeMainFrameToGrid(gameParameters);

            panelContainer.add(minefieldPanel, "minefield");
            cardLayout.show(panelContainer, "minefield");
        });
        menu.add(newGameItem);

        JMenuItem highScoreItem = new JMenuItem("High Scores");
        highScoreItem.addActionListener(e -> {
            mainFrame.setSize(width, height);
            highScore.refreshTable();
            cardLayout.show(panelContainer, "highScorePanel");
        });
        menu.add(highScoreItem);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            String about = String.format("Minesweeper v1.0%n%nАвтор: Косенко А.В.");
            JOptionPane.showMessageDialog(panelContainer, about);
        });
        menu.add(aboutItem);
        menu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(exitItem);

        return menu;
    }

    private JPanel getGreetingsPanel() {
        return new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);

                try {
//                    Image image = ImageIO.read(new File("src/com/kosenko/minesweeper/resources/logo.png"));
                    Image image = ImageIO.read(new File("D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/logo.png"));

                    int x = (this.getWidth() - image.getWidth(null)) / 2;
                    int y = (this.getHeight() - image.getHeight(null)) / 2;
                    graphics.drawImage(image, x, y, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private JPanel getMinefieldPanel(JsonObject gameParameters) {
        int columns = gameParameters.get("columns").getAsInt();
        int rows = gameParameters.get("rows").getAsInt();
        int countMines = gameParameters.get("mines").getAsInt();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, columns, ICON_V_GAP, ICON_H_GAP));

        int[][] minefield = GameController.getMineField(columns, rows, countMines);

        Cell[][] cells = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = new Cell();
                cell.setTile();
                cell.setPositionX(j);
                cell.setPositionY(i);

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (cell.isFlagged()) {
                                cell.setFlagged(false);
                            }

                            int x = cell.getPositionX();
                            int y = cell.getPositionY();

                            if (GameController.isLose(minefield[y][x])) {
                                cell.setMine();

                                JOptionPane.showMessageDialog(panel, "Game over!");

                                mainFrame.setSize(width, height);
                                cardLayout.show(panelContainer, "highScorePanel");
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

                            mainFrame.setSize(width, height);
                            highScore.refreshTable();
                            cardLayout.show(panelContainer, "highScorePanel");
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
}
