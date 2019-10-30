package com.kosenko.minesweeper.models;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String difficulty;

    public User(String name, String difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
