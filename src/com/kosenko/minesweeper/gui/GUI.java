package com.kosenko.minesweeper.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame mainWindow;

    private JPanel cellsPanel;

    private GridBagConstraints constraints;

    private final int cellSize = 50;
    private int countCells = 9;
    private int countMines = 10;

    public GUI() {
        mainWindow = new JFrame("Minesweeper");

//        int width = cellSize * countCells;
//        int height = width + 1;

//        mainWindow.setSize(width, height);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setLayout(new GridLayout());
        mainWindow.setJMenuBar(createMenuBar());

//        JTable test = new JTable();
//        mainWindow.add(test);

//        mainWindow.pack();

        constraints = setConstraints();

        cellsPanel = createCellsPanel();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        constraints.weighty = 1;

        mainWindow.add(cellsPanel, constraints);
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

    private JMenuBar createMenuBar() {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New game");
        fileMenu.add(newGameItem);

        JMenuItem highScoreItem = new JMenuItem("High score");
        fileMenu.add(highScoreItem);

        JMenuItem aboutItem = new JMenuItem("About");
        fileMenu.add(aboutItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        JMenuBar mainMenuBar = new JMenuBar();
        mainMenuBar.add(fileMenu);

        return mainMenuBar;
    }

    private GridBagConstraints setConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

//        constraints.anchor = GridBagConstraints.NORTH;
//        constraints.fill = GridBagConstraints.NONE;
//        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;

        return constraints;
    }

    private JPanel createCellsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                constraints.gridx = j;
                constraints.gridy = i;

                panel.add(new JButton("" + j), constraints);
            }
        }
        return panel;
    }
}
