package controller;

import model.Direction;
import model.GameBoardModel;
import model.GameState;
import model.Ghost;
import model.HighScoresManager;
import model.Player;
import model.Position;
import model.ScoreEntry;
import view.EndGameWindow;
import view.GameWindow;
import view.NameSubmitListener;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameController {
    private final GameBoardModel model;
    private final GameState state;
    private final GameWindow view;
    private Thread ghostThread;
    private Thread powerThread;
    private volatile boolean running;
    private long startTime;
    private final Random random = new Random();

    public GameController(GameBoardModel model, GameWindow view, GameState state) {
        this.model = model;
        this.view = view;
        this.state = state;
        Player player = new Player(new Position(23, 13), 3);
        state.setPlayer(player);
        view.updateScore(state.getScore());
        view.updateHearts(player.getHearts());
        view.updateTime(0);
        createGhosts();
        running = true;
        ghostThread = new Thread(new GhostMover(state, model, view, this));
        powerThread = new Thread(new PowerUpSpawner(state, model, this));
        ghostThread.start();
        powerThread.start();
        setupKeys();
    }

    private void setupKeys() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Direction dir = null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        dir = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        dir = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        dir = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dir = Direction.RIGHT;
                        break;
                    default:
                        break;
                }
                if (dir != null) {
                    if (startTime == 0L) {
                        startTime = System.currentTimeMillis();
                    }
                    state.getPlayer().setDirection(dir);
                    state.movePlayer(dir);
                    model.refresh();
                    view.updateScore(state.getScore());
                }
            }
        });
    }

    private void createGhosts() {
        state.addGhost(new Ghost(new Position(13, 11), "ghostPink.png", 0));
        state.addGhost(new Ghost(new Position(13, 15), "ghostBlue.png", 1));
        state.addGhost(new Ghost(new Position(15, 11), "ghostPurple.png", 2));
        state.addGhost(new Ghost(new Position(15, 15), "ghostMint.png", 3));
    }

    public boolean isRunning() {
        return running;
    }

    public long getStartTime() {
        return startTime;
    }

    public void stopRunning() {
        running = false;
    }

    public void endGame() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            SwingUtilities.invokeLater(() -> {
                view.setVisible(false);
                NameSubmitListener submit = name -> {
                    if (name != null && !name.isEmpty()) {
                        HighScoresManager manager = new HighScoresManager();
                        manager.addScore(new ScoreEntry(name, state.getScore()));
                    }
                    view.dispose();
                };
                EndGameWindow window = new EndGameWindow(state.getScore(), submit);
                window.setVisible(true);
            });
        });
        t.start();
    }
}
