package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import model.BoardMap;


public class Player extends Entity {
    private int hearts;
    private Direction direction;
    private Map<Direction, BufferedImage>sprites;
    private volatile boolean invincible;
    private volatile boolean frozen;
    private long moveDelay = 150;
    private long nextAllowedMove = 0;

    
    public Player(Position position, int hearts) {
        super(position);
        this.hearts = hearts;
        this.sprites = new EnumMap<>(Direction.class);
        this.direction = Direction.DOWN;
        loadSprites();
    }

    
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

    
    public BufferedImage getSprite() {
        BufferedImage original = sprites.get(direction);
        return scale(original, 30);
    }

    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getHearts() {
        return hearts;
    }

    public void loseHeart() {
        hearts--;
    }

    
    public boolean isInvincible() {
        return invincible;
    }

    
    public boolean canMove() {
        long now = System.currentTimeMillis();
        return !frozen && now >= nextAllowedMove;
    }

    
    public void makeInvincible(long millis) {
        invincible = true;
        frozen = true;
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {}
            invincible = false;
            frozen = false;
        }).start();
    }

    
    public void grantTemporaryInvincibility(long millis) {
        invincible = true;
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {}
            invincible = false;
        }).start();
    }

    
    public void speedUp(long newDelay, long duration) {
        moveDelay = newDelay;
        new Thread(() -> {
            try { Thread.sleep(duration); } catch (InterruptedException ignored) {}
            moveDelay = 150;
        }).start();
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
