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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenuController {
    private final MainMenuWindow window;

    public MainMenuController(MainMenuWindow window) {
        this.window = window;

        window.addNewGameListener(e -> startNewGame());
        window.addHighScoresListener(e -> showHighScores());
        window.addExitListener(e -> System.exit(0));
    }

    private void startNewGame() {
        window.setVisible(false);

        BoardMap map = new BoardMap();
        GameState state = new GameState(map);
        GameBoardModel model = new GameBoardModel(state);
        GameWindow gameWindow =  new GameWindow(model, state);
        new GameController(model, gameWindow, state);

        gameWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                   window.setVisible(true);
            }
        });
        gameWindow.setVisible(true);
    }

    private void showHighScores() {
        window.setVisible(false);

        HighScoresManager manager = new HighScoresManager();
        HighScoresWindow scoresWindow = new HighScoresWindow(manager.loadScores());
        scoresWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                window.setVisible(true);
            }
        });
        scoresWindow.setVisible(true);
    }
}
