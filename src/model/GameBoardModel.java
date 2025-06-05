package model;

import javax.swing.table.AbstractTableModel;

/**
 * Table model used by the game board JTable.
 */
public class GameBoardModel extends AbstractTableModel {
    private GameState gameState;

    /**
     * Wraps the current game state for the JTable.
     */
    public GameBoardModel(GameState state) {
        this.gameState = state;
    }

    @Override
    public int getRowCount() {
        return gameState.getBoardMap().getRows();
    }

    @Override
    public int getColumnCount() {
        return gameState.getBoardMap().getCols();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        for (Ghost ghost : gameState.getGhosts()) {
            Position gPos = ghost.getPosition();
            if (gPos.getRow() == rowIndex && gPos.getCol() == columnIndex) {
                return ghost.getSprite();
            }
        }
        for (PowerUp p : gameState.getPowerUps()) {
            Position pos = p.getPosition();
            if (pos.getRow() == rowIndex && pos.getCol() == columnIndex) {
                return p.getSprite();
            }
        }
        Player player = gameState.getPlayer();
        if (player != null)  {
            Position position = player.getPosition();
            if (position.getRow() == rowIndex && position.getCol() == columnIndex) {
                return player.getSprite();
            }
        }
        return gameState.getBoardMap().getTileAt(rowIndex, columnIndex);
    }

    /**
     * Notifies the JTable that data has changed.
     */
    public void refresh() {
        fireTableRowsUpdated(0, getRowCount() - 1);
    }
}
