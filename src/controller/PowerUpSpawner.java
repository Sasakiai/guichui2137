package controller;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Random;
import model.GameBoardModel;
import model.GameState;
import model.Ghost;
import model.Position;
import model.PowerUp;
import model.PowerUpType;

public class PowerUpSpawner extends Thread {
    private final GameState state;
    private final GameBoardModel model;
    private final GameController controller;
    private final Random random = new Random();

    public PowerUpSpawner(GameState state, GameBoardModel model, GameController controller) {
        this.state = state;
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (controller.isRunning()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
            if (!controller.isRunning()) {
                break;
            }
            if (Math.random() < 0.25) {
                spawn();
                SwingUtilities.invokeLater(() -> model.refresh());
            }
        }
    }

    private void spawn() {
        List<Ghost> ghosts = state.getGhosts();
        if (ghosts.isEmpty()) {
            return;
        }
        Ghost g = ghosts.get(random.nextInt(ghosts.size()));
        Position pos = new Position(g.getPosition().getRow(), g.getPosition().getCol());
        PowerUpType[] types = PowerUpType.values();
        PowerUpType type = types[random.nextInt(types.length)];
        state.addPowerUp(new PowerUp(pos, type));
    }
}
