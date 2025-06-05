package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Simple AI controlled enemy.
 */
public class Ghost extends Entity {
    private final BufferedImage sprite;
    private final Random random = new Random();
    private final int order;
    private boolean leavingHouse = true;

    /**
     * Creates a ghost at the given position using the supplied sprite file.
     */
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

    /**
     * @return sprite image used when rendering this ghost
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * @return spawn order used to stagger release
     */
    public int getOrder() {
        return order;
    }

    /**
     * Picks a random direction.
     */
    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    /**
     * Chooses the next move for the ghost.
     */
    public Direction nextDirection(BoardMap map) {
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
