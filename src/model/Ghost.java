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
            // fallback: simple colored circle so game stays playable
            BufferedImage img = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.MAGENTA);
            g2.fillOval(0, 0, 30, 30);
            g2.dispose();
            return img;
        }
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getOrder() {
        return order;
    }

    public boolean isLeavingHouse() {
        return leavingHouse;
    }

    public void setLeavingHouse(boolean leavingHouse) {
        this.leavingHouse = leavingHouse;
    }

    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    public Direction nextDirection(Position exit, BoardMap map) {
        if (leavingHouse) {
            if (position.getRow() > exit.getRow()) return Direction.UP;
            if (position.getRow() < exit.getRow()) return Direction.DOWN;
            if (position.getCol() > exit.getCol()) return Direction.LEFT;
            if (position.getCol() < exit.getCol()) return Direction.RIGHT;
            return Direction.UP; // default, shouldn't happen
        }

        // choose a random valid direction so ghosts keep moving
        for (int i = 0; i < 10; i++) {
            Direction dir = getRandomDirection();
            Position next = position.nextInDirection(dir);
            if (map.getTileAt(next.getRow(), next.getCol()) != TileType.WALL) {
                return dir;
            }
        }
        // fallback if somehow surrounded by walls
        return Direction.UP;
    }
}