package model;

import java.io.Serializable;

/**
 * Stores a player's name and score for persistence.
 */
public class ScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int score;

    /**
     * Creates a score entry.
     */
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * @return stored player name
     */
    public String getName() {
        return name;
    }

    /**
     * @return stored score value
     */
    public int getScore() {
        return score;
    }
}
