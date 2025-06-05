package model;


public abstract class Entity {
    protected Position position;
    protected Direction direction;

    
    public Entity(Position position) {
        this.position = position;
        this.direction = Direction.DOWN;
    }

    
    public Position getPosition() {
        return position;
    }

    
    public Direction getDirection() {
        return direction;
    }

    
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

