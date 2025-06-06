package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Ghost extends Entity {
    private final BufferedImage sprite;
    private final Random random = new Random();
    private final int order;
    private boolean leavingHouse = true;

    public Ghost(Position position, String spriteName, int order) {
        super(position);
        this.order = order;
        this.sprite = loadSprite(spriteName);
    }

    private BufferedImage loadSprite(String name) {
        try {
            return ImageIO.read(getClass().getResource("/" + name));
        } catch (IOException e) {
            return new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getOrder() {
        return order;
    }

    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    public Direction nextDirection(BoardMap map) {
        for (int i = 0; i < 10; i++) {
            Direction dir = getRandomDirection();
            Position next = position.nextInDirection(dir);
            if (map.getTileAt(next.getRow(), next.getCol()) != TileType.WALL) {
                return dir;
            }
        }
        return Direction.UP;
    }
}
