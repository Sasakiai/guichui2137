package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Temporary item that grants special effects.
 */
public class PowerUp {
    private final Position position;
    private final PowerUpType type;
    private final BufferedImage sprite;

    /**
     * Creates a power-up at the given board position.
     */
    public PowerUp(Position position, PowerUpType type) {
        this.position = position;
        this.type = type;
        this.sprite = loadSprite();
    }

    private BufferedImage loadSprite() {
        try {
            return ImageIO.read(getClass().getResource("/powerUp.png"));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @return board position of this power-up
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return type of effect granted
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * @return image shown on the board
     */
    public BufferedImage getSprite() {
        return sprite;
    }
}
