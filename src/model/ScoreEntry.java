package model;

import java.io.Serializable;


public class ScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int score;

    
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    
    public String getName() {
        return name;
    }

    
    public int getScore() {
        return score;
    }
}
