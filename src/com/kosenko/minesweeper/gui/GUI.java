package com.kosenko.minesweeper.gui;

import com.kosenko.minesweeper.controllers.MinefieldController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class GUI {
    private JFrame mainWindow;
    private JPanel cardContainer;
    private CardLayout cardLayout;

    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;
    private final static int MENU_HEIGHT = 30;

    private int iconWidth;
    private int iconHeight;
    private int defaultWidth;
    private int defaultHeight;

    public GUI() {
        iconWidth = Cell.getIconWidth();
        iconHeight = Cell.getIconHeight();

        defaultWidth = iconWidth * MinefieldController.getDefaultColumns();
        defaultHeight = iconHeight * MinefieldController.getDefaultRows();

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

            Map<String, Integer> minefieldParameters = newGameDialog.getMinefieldParameters();

            JPanel minefield = getMinefield(minefieldParameters.get("columns"), minefieldParameters.get("rows"), minefieldParameters.get("countMines"));
            mainWindow.setSize(iconWidth * minefieldParameters.get("columns"), iconHeight * minefieldParameters.get("rows") + MENU_HEIGHT + ICON_H_GAP * minefieldParameters.get("rows"));

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
        GridLayout layout = new GridLayout(rows, columns, ICON_V_GAP, ICON_H_GAP);

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

                        //TODO: Счетчик открытых бомб и функцию выигрыша

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (cell.isFlagged()) {
                                cell.setFlagged(false);
                            }

                            int x = cell.getPositionX();
                            int y = cell.getPositionY();

                            if (minefield[y][x] == MinefieldController.getMineValue()) {
                                cell.setMine();

                                //TODO: Функцию проигрыша

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
        private JTextField playerName;
        private JTextField columns;
        private JTextField rows;
        private JTextField countMine;

        private JLabel nameLabel;
        private JLabel columnsLabel;
        private JLabel rowsLabel;
        private JLabel countMineLabel;

        private JButton ok;
        private JButton cancel;

        private Map<String, Integer> minefieldParameters;

        public NewGameDialog(JFrame parent, String name, boolean modal) {
            super(parent, name, modal);

            setLayout(new GridBagLayout());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            nameLabel = new JLabel("Введите ваше имя:", JLabel.LEFT);
            columnsLabel = new JLabel("Укажите ширину поля:", JLabel.LEFT);
            rowsLabel = new JLabel("Укажите высоту поля:", JLabel.LEFT);
            countMineLabel = new JLabel("Укажите колличество мин на поле:", JLabel.LEFT);

            playerName = new JTextField();
            columns = new JTextField();
            rows = new JTextField();
            countMine = new JTextField();

            minefieldParameters = new HashMap<>();

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

            add(countMineLabel, constraints);
            add(countMine, constraints);
            
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

        public Map<String, Integer> getMinefieldParameters() {
            return minefieldParameters;
        }

        private void setMinefieldParameters(String key, String value) {
            if (value.equals("")) {
                minefieldParameters.put(key, MinefieldController.getDefaultColumns());
            } else if (MinefieldController.isNumber(value)) {
                minefieldParameters.put(key, MinefieldController.getInt(value));
            } else {
                JOptionPane.showMessageDialog(this, "Введите число.");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                setMinefieldParameters("playerName", playerName.getText());
                setMinefieldParameters("columns", columns.getText());
                setMinefieldParameters("rows", rows.getText());
                setMinefieldParameters("countMines", countMine.getText());

                dispose();
            }

            if (e.getSource() == cancel) {
                dispose();
            }
        }
    }
}
