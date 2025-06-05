package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class PowerUp {
    private final Position position;
    private final PowerUpType type;
    private final BufferedImage sprite;

    
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

    
    public Position getPosition() {
        return position;
    }

    
    public PowerUpType getType() {
        return type;
    }

    
    public BufferedImage getSprite() {
        return sprite;
    }
}
