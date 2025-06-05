package controller;

import model.BoardMap;
import model.Direction;
import model.GameBoardModel;
import model.GameState;
import model.Ghost;
import model.HighScoresManager;
import model.Player;
import model.Position;
import model.PowerUp;
import model.PowerUpType;
import model.ScoreEntry;
import view.EndGameWindow;
import view.GameWindow;
import view.NameSubmitListener;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

/**
 * Controls the gameplay loop and reacts to user input.
 */
public class GameController {
    private final GameBoardModel model;
    private final GameState gameState;
    private final GameWindow view;
    private Thread ghostThread;
    private Thread powerUpThread;
    private volatile boolean running;
    private long startTime;
    private final Random random = new Random();

    public GameController(GameBoardModel model, GameWindow view, GameState gameState) {
        this.model = model;
        this.view = view;
        this.gameState = gameState;
        this.startTime = 0L;

        Player player = new Player(new Position(23, 13), 3);
        gameState.setPlayer(player);
        view.updateScore(gameState.getScore());
        view.updateHearts(player.getHearts());
        view.updateTime(0);

        createGhosts();
        startThreads();
        setupKeyListener();
    }

    private void setupKeyListener() {
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
                    gameState.getPlayer().setDirection(dir);
                    gameState.movePlayer(dir);
                    model.refresh();
                    view.updateScore(gameState.getScore());
                }
            }
        });
    }

    private void createGhosts() {
        gameState.addGhost(new Ghost(new Position(13, 11), "ghostPink.png", 0));
        gameState.addGhost(new Ghost(new Position(13, 15), "ghostBlue.png", 1));
        gameState.addGhost(new Ghost(new Position(15, 11), "ghostPurple.png", 2));
        gameState.addGhost(new Ghost(new Position(15, 15), "ghostMint.png", 3));
    }

    private void startThreads() {
        running = true;
        startGhostThread();
        startPowerUpThread();
    }

    private void startGhostThread() {
        ghostThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    gameState.moveGhosts();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            model.refresh();
                            view.updateScore(gameState.getScore());
                            view.updateHearts(gameState.getPlayer().getHearts());
                            if (startTime != 0L) {
                                long elapsed = System.currentTimeMillis() - startTime;
                                view.updateTime(elapsed);
                            }
                        }
                    });

                    if (gameState.isGameOver()) {
                        running = false;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                endGame();
                            }
                        });
                        break;
                    }

                    try {
                        Thread.sleep(gameState.getGhostMoveDelay());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        ghostThread.start();
    }

    private void startPowerUpThread() {
        powerUpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    if (!running) {
                        break;
                    }

                    if (Math.random() < 0.25) {
                        spawnPowerUp();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                model.refresh();
                            }
                        });
                    }
                }
            }
        });
        powerUpThread.start();
    }

    private void spawnPowerUp() {
        List<Ghost> ghosts = gameState.getGhosts();
        if (ghosts.isEmpty()) {
            return;
        }
        Ghost g = ghosts.get(random.nextInt(ghosts.size()));
        Position pos = new Position(g.getPosition().getRow(), g.getPosition().getCol());
        PowerUpType[] types = PowerUpType.values();
        PowerUpType type = types[random.nextInt(types.length)];
        gameState.addPowerUp(new PowerUp(pos, type));
    }

    private void endGame() {
        Thread delayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisible(false);
                        NameSubmitListener submit = new NameSubmitListener() {
                            @Override
                            public void onSubmit(String playerName) {
                                String name = (playerName == null || playerName.trim().isEmpty())
                                        ? "Player" : playerName;
                                HighScoresManager manager = new HighScoresManager();
                                manager.addScore(new ScoreEntry(name, gameState.getScore()));
                                view.dispose();
                            }
                        };
                        EndGameWindow window = new EndGameWindow(gameState.getScore(), submit);
                        window.setVisible(true);
                    }
                });
            }
        });
        delayThread.start();
    }
}

