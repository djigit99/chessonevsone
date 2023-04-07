package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitToMakeMoveState extends ChessBoard.ChessBoardState {
    private CellModel.Coords pieceToMove;
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public WaitToMakeMoveState(ChessBoard board) {
        super(board);
    }

    public void setPieceToMove(CellModel.Coords pieceToMove) {
        this.pieceToMove = pieceToMove;
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        this.coordsToCellListeners = coordsToCellListeners;
    }

    @Override
    public void doOnUpdate(CellModel.Coords coords) {
        Cell acquiredCell = coordsToCellListeners.get(pieceToMove).getLeft();
        CellListener acquiredCellListener = coordsToCellListeners.get(pieceToMove).getRight();
        Cell cell = coordsToCellListeners.get(coords).getLeft();
        CellModel.State cellState = cell.getCellViewModel().getModel().getState();

        if (cellState.equals(CellModel.State.RELEASED)) {
            boolean isMovePossible = getBoard().isMovePossible(acquiredCell.getPiece(), pieceToMove, coords);
            if (isMovePossible) {
                getBoard().makeMove(pieceToMove, coords);
                getBoard().getPlayerListener().onMakeMove(
                        acquiredCell.getCellViewModel().getModel().getCoords(),
                        cell.getCellViewModel().getModel().getCoords());
                changeState(new WaitForOpponentMoveState(getBoard()));
            } else {
                changeState(new WaitForSelectedPieceState(getBoard()));
            }
        } else {
            changeState(new WaitForSelectedPieceState(getBoard()));
        }

        // common for both cell states
        acquiredCellListener.onUpdateFromBoard(CellModel.State.RELEASED);
    }

    @Override
    public void doOnUpdateFromPlayer() {
        // do nothing
    }
}
