package model;

/**
 * Base class for all moving things on the board.
 */
public abstract class Entity {
    protected Position position;
    protected Direction direction;

    /**
     * Creates an entity at the given position facing down.
     */
    public Entity(Position position) {
        this.position = position;
        this.direction = Direction.DOWN;
    }

    /**
     * @return current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return current direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Moves one tile in the provided direction if not blocked by a wall.
     */
    public boolean move(Direction dir, BoardMap map) {
        this.direction = dir;
        Position nextP = position.nextInDirection(dir);
        TileType nextTile = map.getTileAt(nextP.getRow(), nextP.getCol());

        if (nextTile != TileType.WALL) {
            position = nextP;
            return true;
        }
        return false;
    }
}

