package dev.djigit.chessonevsone.chessboard.state;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitForSelectedPieceState extends ChessBoardState {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private CellModel.State cellState;
    private CellModel.Coords pieceToMove;

    public WaitForSelectedPieceState(ChessBoard board) {
        super(board);
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
        ImmutablePair<Cell, CellListener> cellCellListener = coordsToCellListeners.get(coords);
        Cell cell = cellCellListener.getLeft();
        CellListener cellListener = cellCellListener.getRight();
        if (cellState.equals(CellModel.State.RELEASED)) {
            if (!(cell.hasPiece() && cell.isFriendPiece(getBoard().getPlayerColor()))) // not possible first click
                return;
            cellListener.onUpdateFromBoard(CellModel.State.ACQUIRED);
            pieceToMove = coords;

            WaitToMakeMoveState waitToMakeMoveState = new WaitToMakeMoveState(getBoard());
            waitToMakeMoveState.setPieceToMove(pieceToMove);
            getBoard().changeState(waitToMakeMoveState);
        } else {
            throw new IllegalStateException("Illegal cell state");
        }
    }
}
