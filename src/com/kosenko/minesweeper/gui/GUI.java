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

    private MinefieldController minefieldController;

    private final static int ICON_WIDTH = 40;
    private final static int ICON_HEIGHT = 40;
    private final static int ICON_V_GAP = 0;
    private final static int ICON_H_GAP = 0;
    private final static int MENU_HEIGHT = 30;

    private int defaultWidth;
    private int defaultHeight;

    public GUI() {
        minefieldController = new MinefieldController();
        defaultWidth = ICON_WIDTH * minefieldController.getDefaultColumns();
        defaultHeight = ICON_HEIGHT * minefieldController.getDefaultRows();

        cardLayout = new CardLayout();

        cardContainer = new JPanel();
        cardContainer.setLayout(cardLayout);
        cardContainer.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        cardContainer.add(getGreetingsPanel(), "greetings");

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

//            System.out.println(Arrays.toString(newGameDialog.getDialogData()));
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

    private JPanel getMinefield(int row, int cols, int countBomb) {
        GridLayout layout = new GridLayout(row, cols, ICON_H_GAP, ICON_V_GAP);

        JPanel panel = new JPanel();
        panel.setLayout(layout);

        Icon tile = new ImageIcon("src/com/kosenko/minesweeper/resources/tile2.png");
        Icon rollOver = new ImageIcon("src/com/kosenko/minesweeper/resources/rollOverTile.png");
        Icon bomb = new ImageIcon("src/com/kosenko/minesweeper/resources/mine.png");
        Icon flag = new ImageIcon("src/com/kosenko/minesweeper/resources/flag1.png");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton(tile);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setRolloverIcon(rollOver);
                button.setSize(ICON_WIDTH, ICON_HEIGHT);

                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> {
                    System.out.println("i: " + finalI + " j: " + finalJ);



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
                dialogData[index] = minefieldController.getDefaultColumns();
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

