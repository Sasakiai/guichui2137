package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GameState {
    private BoardMap boardMap;
    private Player player;
    private int score;
    private final List<Ghost> ghosts = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private boolean doublePoints = false;
    private boolean ghostSlow = false;
    private boolean gameOver = false;

    
    public GameState(BoardMap boardMap) {
        this.boardMap = boardMap;
        this.score = 0;
    }

    
    public void setPlayer(Player player) {
        this.player = player;
    }

    
    public Player getPlayer() {
        return player;
    }

    
    public int getScore() {
        return score;
    }

    
    public BoardMap getBoardMap() {
        return boardMap;
    }

    
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    
    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
    }

    public void removePowerUp(PowerUp p) {
        powerUps.remove(p);
    }

    
    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    
    public boolean isGameOver() {
        return gameOver;
    }

    
    public int getGhostMoveDelay() {
        return ghostSlow ? 400 : 400;
    }

    
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

    
    public void moveGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.move(ghost.nextDirection(boardMap), boardMap);
        }
        checkCollisions();
    }

    
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

                for (int i = 0; i < powerUps.size(); i++) {
                    PowerUp p = powerUps.get(i);
                    System.out.println(p.getPosition().col + " " + p.getPosition().row);
                    System.out.println(newPos.col + " " + newPos.row);
                    if (p.getPosition().getCol() == newPos.getCol() && p.getPosition().getRow() == newPos.getRow()) {
                        applyPowerUp(p);
                        powerUps.remove(i);
                        break;
                    }
                }
            }
            checkCollisions();
            return moved;
        }
        return false;
    }

    
    private boolean isDoublePointsActive() {
        return doublePoints;
    }

    
    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case SPEED_BOOST:
                player.speedUp(80, 5000);
                break;
            case GHOST_SLOW:
                ghostSlow = true;
                new Thread(() -> {
                    try { Thread.sleep(5000); } catch (InterruptedException e) {}
                    ghostSlow = false;
                }).start();
                break;
            case DOUBLE_POINTS:
                doublePoints = true;
                new Thread(() -> {
                    try { Thread.sleep(3000); } catch (InterruptedException e) {}
                    doublePoints = false;
                }).start();
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
