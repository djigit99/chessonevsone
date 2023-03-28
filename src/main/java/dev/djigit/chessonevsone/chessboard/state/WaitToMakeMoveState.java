package dev.djigit.chessonevsone.chessboard.state;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitToMakeMoveState extends ChessBoardState {
    private CellModel.Coords pieceToMove;
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public WaitToMakeMoveState(ChessBoard chessBoard) {
        super(chessBoard);
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
            }
        }

        // common for both cell states
        acquiredCellListener.onUpdateFromBoard(CellModel.State.RELEASED);

        getBoard().changeState(new WaitForSelectedPieceState(getBoard()));
    }
}
