package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitForSelectedPieceState extends ChessBoard.ChessBoardState {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public WaitForSelectedPieceState(ChessBoard board) {
        super(board);
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        this.coordsToCellListeners = coordsToCellListeners;
    }

    @Override
    public void doOnUpdate(CellModel.Coords coords) {
        ImmutablePair<Cell, CellListener> cellCellListener = coordsToCellListeners.get(coords);
        Cell cell = cellCellListener.getLeft();
        CellListener cellListener = cellCellListener.getRight();
        CellModel.State cellState = cell.getCellViewModel().getModel().getState();

        if (cellState.equals(CellModel.State.RELEASED)) {
            if (!(cell.hasPiece() && cell.isFriendPiece(getBoard().getPlayerColor()))) // not possible first click
                return;
            cellListener.onUpdateFromBoard(CellModel.State.ACQUIRED);

            WaitToMakeMoveState waitToMakeMoveState = new WaitToMakeMoveState(getBoard());
            waitToMakeMoveState.setPieceToMoveCoords(coords);
            changeState(waitToMakeMoveState);
        } else {
            throw new IllegalStateException("Illegal cell state");
        }
    }

    @Override
    public void doOnUpdateFromPlayer() {
        // do nothing
    }

    @Override
    public void doOnUpdateFromChoosePiecePopup(Piece piece) {
        // do nothing
    }
}
