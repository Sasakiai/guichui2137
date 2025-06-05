package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

// Added for custom movement handling
import model.BoardMap;

/**
 * Represents the player's character.
 */
public class Player extends Entity {
    private int hearts;
    private Direction direction;
    private Map<Direction, BufferedImage>sprites;
    private long invincibleUntil;
    private long frozenUntil;
    private long moveDelay = 150;
    private long moveDelayResetTime = 0;
    private long nextAllowedMove = 0;

    /**
     * Creates a player at the given position with the specified number of lives.
     */
    public Player(Position position, int hearts) {
        super(position);
        this.hearts = hearts;
        this.sprites = new EnumMap<>(Direction.class);
        this.direction = Direction.DOWN;
        loadSprites();
    }

    /**
     * Loads Pac-Man sprites from resources.
     */
    private void loadSprites() {
        try {
            sprites.put(Direction.UP, ImageIO.read(getClass().getResource("/pacBack.png")));
            sprites.put(Direction.LEFT, ImageIO.read(getClass().getResource("/pacLeft.png")));
            sprites.put(Direction.RIGHT, ImageIO.read(getClass().getResource("/pacRight.png")));
            sprites.put(Direction.DOWN, ImageIO.read(getClass().getResource("/pacFront.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage scale(BufferedImage original, int size) {
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, size, size, null);
        g2.dispose();
        return scaled;
    }

    /**
     * @return scaled sprite for the current direction
     */
    public BufferedImage getSprite() {
        BufferedImage original = sprites.get(direction);
        return scale(original, 30);
    }

    /**
     * Sets the direction the player is facing.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getHearts() {
        return hearts;
    }

    public void loseHeart() {
        hearts--;
    }

    /**
     * @return true if the player is currently invincible
     */
    public boolean isInvincible() {
        return System.currentTimeMillis() < invincibleUntil;
    }

    /**
     * Checks if the player can perform a move at this time.
     */
    public boolean canMove() {
        long now = System.currentTimeMillis();
        if (moveDelayResetTime > 0 && now >= moveDelayResetTime) {
            moveDelay = 150;
            moveDelayResetTime = 0;
        }
        return now >= frozenUntil && now >= nextAllowedMove;
    }

    /**
     * Freezes and protects the player for a short time.
     */
    public void makeInvincible(long millis) {
        long now = System.currentTimeMillis();
        this.invincibleUntil = now + millis;
        this.frozenUntil = now + millis;
    }

    /**
     * Grants invincibility without freezing movement.
     */
    public void grantTemporaryInvincibility(long millis) {
        this.invincibleUntil = System.currentTimeMillis() + millis;
    }

    /**
     * Temporarily increases movement speed.
     */
    public void speedUp(long newDelay, long duration) {
        moveDelay = newDelay;
        moveDelayResetTime = System.currentTimeMillis() + duration;
    }

    @Override
    public boolean move(Direction dir, BoardMap map) {
        boolean moved = super.move(dir, map);
        if (moved) {
            nextAllowedMove = System.currentTimeMillis() + moveDelay;
        }
        return moved;
    }
}
