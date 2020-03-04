package com.kosenko.minesweeper.gui;

import com.kosenko.minesweeper.controllers.MinefieldController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {
    private JFrame mainWindow;
    private JPanel container;
    private  CardLayout cardLayout;

    private MinefieldController minefieldController;

    private final static int ICON_WIDTH = 40;
    private final static int ICON_HEIGHT = 40;
    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;

    public GUI() {
        cardLayout = new CardLayout();

        container = new JPanel();
        container.setLayout(cardLayout);

        minefieldController = new MinefieldController();

        int panelWidth = ICON_WIDTH * minefieldController.getDefaultWidth();
        int panelHeight = ICON_HEIGHT * minefieldController.getDefaultHeight();

        container.setPreferredSize(new Dimension(panelWidth, panelHeight));
        container.add(getGreetingsPanel(), "greetings");

        mainWindow = new JFrame("Minesweeper");
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setJMenuBar(getMainMenu());
        mainWindow.add(container);
    }

    public void showGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Error");
            }

            mainWindow.setVisible(true);
            mainWindow.pack();
        });
    }

    private JMenuBar getMainMenu() {
        JMenu file = new JMenu("File");

        JMenuItem newGame = new JMenuItem("New game");
        file.add(newGame);

        newGame.addActionListener(e -> {
            String[][] minefield = MinefieldController.getMineField();
            //Окно с вводом размера поля и колличеством бомб
            container.add(getMinefield(9, 9), "minefield");
            cardLayout.show(container, "minefield");

            System.out.println("Game started!");
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
        menuBar.add(file);

        return menuBar;
    }

    private JPanel getGreetingsPanel() {
//        JPanel greetings = new JPanel();

        return new JPanel();
    }

    private JPanel getMinefield(int row, int cols) {
        GridLayout layout = new GridLayout(row, cols, ICON_H_GAP, ICON_V_GAP);

        JPanel panel = new JPanel();
        panel.setLayout(layout);

        Icon tile = new ImageIcon("src/com/kosenko/minesweeper/resources/tile2.png");
        Icon rollOver = new ImageIcon("src/com/kosenko/minesweeper/resources/rollOverTile.png");
        Icon bomb = new ImageIcon("src/com/kosenko/minesweeper/resources/mine.png");
        Icon flag = new ImageIcon("src/com/kosenko/minesweeper/resources/flag1.png");

        for (int i = 0; i < row * cols; i++) {
            JButton button = new JButton(tile);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setRolloverIcon(rollOver);
            button.setPreferredSize(new Dimension(ICON_WIDTH, ICON_HEIGHT));

            button.addActionListener(e -> {
                System.out.printf("X: %d Y: %d%n", button.getX(), button. getY());

                button.setEnabled(false);
                button.setDisabledIcon(bomb);
            });

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                        button.setIcon(flag);
                        button.setRolloverIcon(flag);
                    }
                }
            });

            panel.add(button);
        }

        return panel;
    }
}
