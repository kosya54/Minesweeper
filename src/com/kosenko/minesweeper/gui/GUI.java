package com.kosenko.minesweeper.gui;

import com.kosenko.minesweeper.controllers.MinefieldController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {
    private JFrame mainWindow;
    private JPanel container;
    private CardLayout cardLayout;

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
            mainWindow.setLocationRelativeTo(null);
        });
    }

    private JMenuBar getMainMenu() {
        JMenu file = new JMenu("File");

        JMenuItem newGame = new JMenuItem("New game");
        file.add(newGame);

        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewGameDialog newGameDialog = new NewGameDialog(mainWindow, "New game");
                newGameDialog.setVisible(true);

                System.out.println(newGameDialog.getDialogData().toString());
            }
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

        Icon tile = new ImageIcon("D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/tile2.png");
        Icon rollOver = new ImageIcon("D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/rollOverTile.png");
        Icon bomb = new ImageIcon("D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/mine.png");
        Icon flag = new ImageIcon("D:/Java/Minesweeper/src/com/kosenko/minesweeper/resources/flag1.png");

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

    private class NewGameDialog extends JDialog implements ActionListener {
        private JTextField width;
        private JTextField height;
        private JTextField countBomb;
        private int[] dialogData;

        private final static int FIELD_SIZE = 20;

        private JButton ok;
        private JButton cancel;

        public NewGameDialog(JFrame parent, String name) {
            super(parent, name);
         
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

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                if (width.getText().equals("")) {
                    dialogData[0] = minefieldController.getDefaultWidth();
                }

                if (height.getText().equals("")) {
                    dialogData[1] = minefieldController.getDefaultHeight();
                }

                if (countBomb.getText().equals("")) {
                    dialogData[2] = minefieldController.getDefaultCountMines();
                }

                if (MinefieldController.isNumber(width.getText()) && MinefieldController.isNumber(height.getText()) && MinefieldController.isNumber(countBomb.getText())) {
                    System.out.println(MinefieldController.isNumber(width.getText()));
                    try {
                        dialogData = MinefieldController.getMineFieldParam(width.getText(), height.getText(), countBomb.getText());
                    } catch(NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage() + "111");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Введите число.");

                    return;
                }
            }

            if (e.getSource() == cancel) {
                System.exit(0);
            }
            
            dispose();
        }

/*        public void showNewGameDialog() {
            add(width);
            add(height);
            add(countBomb);
            add(ok);
            add(cancel);

            GridLayout layout = new GridLayout(5, 1, ICON_H_GAP, ICON_V_GAP);

            setLayout(layout);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(200, 200);
            setVisible(true);
            setLocationRelativeTo(null);
        } */
    }

/*    private Dialog getNewGameDialog() {
        JDialog newGame = new JDialog(mainWindow, "New Game");
        
        JTextField width = new JTextField(20);
        JTextField height = new JTextField(20);
        JTextField countBomb = new JTextField(20);
        
        JButton ok = new JButton("ok");
        JButton cancel = new JButton("cancel");

        newGame.add(width);
        newGame.add(height);
        newGame.add(countBomb);
        newGame.add(ok);
        newGame.add(cancel);

        GridLayout layout = new GridLayout(5, 1, ICON_H_GAP, ICON_V_GAP);

        newGame.setLayout(layout);
        newGame.setLocationRelativeTo(null);
        newGame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newGame.setSize(200, 200);
//        newGame.setVisible(true);
//        newGame.pack();
        newGame.setLocationRelativeTo(null);

        return newGame;
    } */
}

