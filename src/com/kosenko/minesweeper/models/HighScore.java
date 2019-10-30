package com.kosenko.minesweeper.models;

import java.io.*;
import java.util.ArrayList;

public class HighScore {
    private ArrayList<User> userList;
    private String file = "D:\\__Java\\IdeaProjects\\Minesweeper\\src\\com\\kosenko\\minesweeper\\saves\\save.sav";

    public HighScore(ArrayList<User> userList) {
        this.userList = userList;
    }

    public void saveHighScore() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userList);
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
    }

    public ArrayList<User> loadHighScore() {
        ArrayList<User> newUserList = new ArrayList<>();

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            newUserList = (ArrayList<User>)in.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(ex);
        }
        return newUserList;
    }
}
