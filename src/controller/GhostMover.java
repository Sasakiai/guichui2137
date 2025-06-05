package controller;

import javax.swing.SwingUtilities;
import model.GameBoardModel;
import model.GameState;
import view.GameWindow;

public class GhostMover implements Runnable {
    private final GameState state;
    private final GameBoardModel model;
    private final GameWindow view;
    private final GameController controller;

    public GhostMover(GameState state, GameBoardModel model, GameWindow view, GameController controller) {
        this.state = state;
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (controller.isRunning()) {
            state.moveGhosts();
            SwingUtilities.invokeLater(() -> {
                model.refresh();
                view.updateScore(state.getScore());
                view.updateHearts(state.getPlayer().getHearts());
                if (controller.getStartTime() != 0L) {
                    long elapsed = System.currentTimeMillis() - controller.getStartTime();
                    view.updateTime(elapsed);
                }
            });
            if (state.isGameOver()) {
                controller.stopRunning();
                SwingUtilities.invokeLater(controller::endGame);
                break;
            }
            try {
                Thread.sleep(state.getGhostMoveDelay());
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
