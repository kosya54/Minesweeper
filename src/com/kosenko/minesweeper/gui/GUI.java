package com.kosenko.minesweeper.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame mainWindow;
    private GridBagConstraints constraints;

    public GUI() {
        mainWindow = new JFrame("Minesweeper");

        mainWindow.setResizable(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setJMenuBar(createMenuBar());
        mainWindow.add(minefieldPanel(9, 9));
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
        JMenu file = new JMenu("File");

        JMenuItem newGame = new JMenuItem("New game");
        file.add(newGame);

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

    private JPanel minefieldPanel(int row, int cols) {
        int hgap = 0;
        int vgap = 0;
        GridLayout layout = new GridLayout(row, cols, hgap, vgap);

        JPanel panel = new JPanel();

        int iconWidth = 40;
        int iconHeight = 40;
        int width = iconWidth * row + hgap * (row - 1);
        int height = iconHeight * cols + vgap * (cols - 1);;

        panel.setPreferredSize(new Dimension(width, height));
        panel.setLayout(layout);

        String iconName = "D:\\__Java\\IdeaProjects\\Minesweeper\\src\\com\\kosenko\\minesweeper\\resources\\tile2.png";
        Icon tile = new ImageIcon(iconName);
        for (int i = 0; i < row * cols; i++) {
            JButton button = new JButton(tile);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(iconWidth, iconHeight));

            panel.add(button);
        }

        return panel;
    }

/*    private JPanel createGridPanel() {
        Image bomb = new ImageIcon("D:\\__Java\\IdeaProjects\\Minesweeper\\src\\com\\kosenko\\minesweeper\\resources\\bomb.png").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(bomb, 0, 0, this);
            }
        };

        panel.setPreferredSize(new Dimension(500, 500));

        return panel;
    } */
}
