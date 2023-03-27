package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.cell.CellModel.State;
import dev.djigit.chessonevsone.game.Player;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class ChessBoardListener {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private boolean wasAcquired = false;
    private CellModel.Coords pieceToMove;
    private ChessBoard board;

    public ChessBoardListener(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners, ChessBoard board) {
        this.coordsToCellListeners = coordsToCellListeners;
        this.board = board;
    }

    public void onUpdateReceived(State cellState, CellModel.Coords coords) {
        if (!wasAcquired) {
            ImmutablePair<Cell, CellListener> cellCellListener = coordsToCellListeners.get(coords);
            Cell cell = cellCellListener.getLeft();
            CellListener cellListener = cellCellListener.getRight();
            if (cellState.equals(State.RELEASED)) {
                if (!(cell.hasPiece() && cell.isFriendPiece(board.getPlayerColor()))) // not possible first click
                    return;
                cellListener.onUpdateFromBoard(State.ACQUIRED);
                pieceToMove = coords;
                wasAcquired = true;
            } else {
                throw new IllegalStateException("Illegal cell state");
            }
        } else {
            Cell acquiredCell = coordsToCellListeners.get(pieceToMove).getLeft();
            CellListener acquiredCellListener = coordsToCellListeners.get(pieceToMove).getRight();

            if (cellState.equals(State.RELEASED)) {
                boolean isMovePossible = board.isMovePossible(acquiredCell.getPiece(), pieceToMove, coords);
                if (isMovePossible) {
                    board.makeMove(pieceToMove, coords); // change ImageView for two cells (from and to)
                }
            }

            // common for both cell states
            acquiredCellListener.onUpdateFromBoard(State.RELEASED); // send FROM ACQUIRED TO RELEASED
            wasAcquired = false;
            pieceToMove = null;
        }
    }
}
