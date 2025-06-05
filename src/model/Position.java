package model;

/**
 * Simple holder for board coordinates.
 */
public class Position {
    public int row;
    public int col;

    /**
     * Creates a position with row and column values.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return a new position moved one step in the given direction.
     */
    public Position nextInDirection(Direction dir) {
        switch (dir) {
            case UP:
                return new Position(row - 1, col);
            case DOWN:
                return new Position(row + 1, col);
            case LEFT:
                return new Position(row, col - 1);
            case RIGHT:
                return new Position(row, col + 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return row index
     */
    public int getRow() { return row; }

    /**
     * @return column index
     */
    public int getCol() { return col; }
}

