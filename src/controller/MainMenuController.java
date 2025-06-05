package controller;

import model.BoardMap;
import model.GameBoardModel;
import model.GameState;
import model.HighScoresManager;
import view.GameWindow;
import view.HighScoresWindow;
import view.MainMenuWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles user interaction with the main menu.
 */
public class MainMenuController {
    private final MainMenuWindow window;

    public MainMenuController(MainMenuWindow window) {
        this.window = window;

        window.addNewGameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        window.addHighScoresListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });

        window.addExitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Begins a new game and opens the game window.
     */
    private void startNewGame() {
        window.setVisible(false);

        BoardMap map = new BoardMap();
        GameState state = new GameState(map);
        GameBoardModel model = new GameBoardModel(state);
        GameWindow gameWindow =  new GameWindow(model, state);
        new GameController(model, gameWindow, state);

        gameWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                   window.setVisible(true);
            }
        });
        gameWindow.setVisible(true);
    }

    /**
     * Displays the saved high scores.
     */
    private void showHighScores() {
        window.setVisible(false);

        HighScoresManager manager = new HighScoresManager();
        HighScoresWindow scoresWindow = new HighScoresWindow(manager.loadScores());
        scoresWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                window.setVisible(true);
            }
        });
        scoresWindow.setVisible(true);
    }
}
