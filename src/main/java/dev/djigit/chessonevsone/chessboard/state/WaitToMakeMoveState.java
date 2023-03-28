package dev.djigit.chessonevsone.chessboard.state;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.state.ChessBoardState;
import dev.djigit.chessonevsone.chessboard.state.WaitForSelectedPieceState;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitToMakeMoveState extends ChessBoardState {
    private CellModel.Coords pieceToMove;
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private CellModel.State cellState;

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
    public void setCellState(CellModel.State cellState) {
        this.cellState = cellState;
    }

    @Override
    void doOnUpdate(CellModel.Coords coords) {
        Cell acquiredCell = coordsToCellListeners.get(pieceToMove).getLeft();
        CellListener acquiredCellListener = coordsToCellListeners.get(pieceToMove).getRight();

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
