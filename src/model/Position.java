package model;


public class Position {
    public int row;
    public int col;

    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    
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

    
    public int getRow() { return row; }

    
    public int getCol() { return col; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}

