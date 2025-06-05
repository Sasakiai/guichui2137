package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Holds all mutable information about the current game.
 */
public class GameState {
    private BoardMap boardMap;
    private Player player;
    private int score;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private long doublePointsUntil = 0;
    private long ghostSlowUntil = 0;
    private boolean gameOver = false;

    /**
     * Creates a new state for the given board.
     */
    public GameState(BoardMap boardMap) {
        this.boardMap = boardMap;
        this.score = 0;
    }

    /**
     * Sets the controlled player.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return map with all board tiles
     */
    public BoardMap getBoardMap() {
        return boardMap;
    }

    /**
     * @return list of ghosts currently active
     */
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    /**
     * @return power-ups present on the board
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Adds a power-up to the board.
     */
    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
    }

    public void removePowerUp(PowerUp p) {
        powerUps.remove(p);
    }

    /**
     * Adds a ghost to the state.
     */
    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    /**
     * @return true when the player has lost or cleared the board
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return milliseconds ghosts should wait between moves
     */
    public int getGhostMoveDelay() {
        if (System.currentTimeMillis() < ghostSlowUntil) {
            return 400;
        }
        return 200;
    }

    /**
     * Checks collisions between the player and ghosts.
     */
    private void checkCollisions() {
        if (player == null) return;
        for (Ghost ghost : ghosts) {
            if (ghost.getPosition().getRow() == player.getPosition().getRow() &&
                    ghost.getPosition().getCol() == player.getPosition().getCol()) {
                if (!player.isInvincible()) {
                    player.loseHeart();
                    player.makeInvincible(2000);
                    if (player.getHearts() <= 0) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    /**
     * Moves all ghosts one step and checks collisions.
     */
    public void moveGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.move(ghost.nextDirection(boardMap), boardMap);
        }
        checkCollisions();
    }

    /**
     * Attempts to move the player in the chosen direction.
     */
    public boolean movePlayer(Direction direction) {
        if (player != null) {
            if (!player.canMove()) {
                return false;
            }
            boolean moved = player.move(direction, boardMap);
            if (moved) {
                Position newPos = player.getPosition();
                TileType tile = boardMap.getTileAt(newPos.getRow(), newPos.getCol());
                if (tile == TileType.DOT) {
                    boardMap.setTileAt(newPos.getRow(), newPos.getCol(), TileType.EMPTY);
                    int points = isDoublePointsActive() ? 20 : 10;
                    score += points;
                    if (!boardMap.hasDotsRemaining()) {
                        gameOver = true;
                    }
                }

                Iterator<PowerUp> it = powerUps.iterator();
                while (it.hasNext()) {
                    PowerUp p = it.next();
                    Position pp = p.getPosition();
                    if (pp.getRow() == newPos.getRow() && pp.getCol() == newPos.getCol()) {
                        applyPowerUp(p);
                        it.remove();
                        break;
                    }
                }
            }
            checkCollisions();
            return moved;
        }
        return false;
    }

    /**
     * @return true if double points power-up is active
     */
    private boolean isDoublePointsActive() {
        return System.currentTimeMillis() < doublePointsUntil;
    }

    /**
     * Applies the effect of a collected power-up.
     */
    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case SPEED_BOOST:
                player.speedUp(80, 5000);
                break;
            case GHOST_SLOW:
                ghostSlowUntil = System.currentTimeMillis() + 5000;
                break;
            case DOUBLE_POINTS:
                doublePointsUntil = System.currentTimeMillis() + 3000;
                break;
            case BONUS_POINTS:
                score += 100;
                break;
            case INVINCIBILITY:
                player.grantTemporaryInvincibility(3000);
                break;
            default:
                break;
        }
    }
}
