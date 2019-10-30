package com.kosenko.minesweeper;

import com.kosenko.minesweeper.models.Cell;
import com.kosenko.minesweeper.models.Minefield;

public class Main {
    public static void main(String[] args) {
        int width = 9;
        int height = 9;
        int countMines = 10;

        Cell cell = new Cell();
        Minefield minefield = new Minefield(width, height, countMines, cell);

        String[][] array = minefield.generateMinefield();

        for (String[] arr : array) {
            for (String value : arr) {
                System.out.print(value);
            }
            System.out.println();
        }
/*        GUI gui = new GUI();
        gui.showGUI(); */

/*        Mines mines = new Mines(9, 9, 10);

        int i = 0;
        boolean isOk = true;
        while (i < 10000) {
            int[] array = mines.generateMines();

            isOk = mines.testPrintMines(array);
            if (!isOk) {
                break;
            }
            ++i;
        }

        if (isOk) {
            System.out.println("Ok");
        } else {
            System.out.println("No");
        }

        int j = 0;
        while (j < 50) {
            int[] array = mines.generateMines();
            mines.printMines(array);
            ++j;
        }

        User user1 = new User("Test1", "Beginner");
        User user2 = new User("Test2", "Beginner");
        User user3 = new User("Test3", "Beginner");

        ArrayList<User> userList = new ArrayList<>();

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        HighScore highScore = new HighScore(userList);
        highScore.saveHighScore();

        ArrayList<User> newUserList = highScore.loadHighScore();

        for (User user : newUserList) {
            System.out.printf("Name: %s, Difficulty: %s%n", user.getName(), user.getDifficulty());
        } */
    }
}
