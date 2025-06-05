package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Saves and loads high score entries from disk.
 */
public class HighScoresManager {
    private static final String FILE_NAME = "highscores.ser";

    /**
     * Reads stored scores from a file.
     */
    public List<ScoreEntry> loadScores() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                //noinspection unchecked
                return (List<ScoreEntry>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Writes the given scores to disk.
     */
    public void saveScores(List<ScoreEntry> scores) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new score entry and keeps the list sorted.
     */
    public void addScore(ScoreEntry entry) {
        List<ScoreEntry> scores = loadScores();
        scores.add(entry);
        Collections.sort(scores, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry a, ScoreEntry b) {
                return Integer.compare(b.getScore(), a.getScore());
            }
        });
        saveScores(scores);
    }
}
