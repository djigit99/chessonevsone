package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitToMakeMoveState extends ChessBoard.ChessBoardState {
    private CellModel.Coords pieceToMoveCoords;
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public WaitToMakeMoveState(ChessBoard board) {
        super(board);
    }

    public void setPieceToMoveCoords(CellModel.Coords pieceToMoveCoords) {
        this.pieceToMoveCoords = pieceToMoveCoords;
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        this.coordsToCellListeners = coordsToCellListeners;
    }

    @Override
    public void doOnUpdateFromCell(CellModel.Coords coords) {

        Cell acquiredCell = coordsToCellListeners.get(pieceToMoveCoords).getLeft();
        CellListener acquiredCellListener = coordsToCellListeners.get(pieceToMoveCoords).getRight();
        Cell cell = coordsToCellListeners.get(coords).getLeft();
        CellModel.State cellState = cell.getCellViewModel().getModel().getState();

        if (cellState.equals(CellModel.State.RELEASED)) {
            boolean isMovePossible = getBoard().isMovePossible(acquiredCell.getPiece(), pieceToMoveCoords, coords);
            if (isMovePossible) {
                boolean doesMoveNeedConfirm = getBoard().makeMove(pieceToMoveCoords, coords);

                if (doesMoveNeedConfirm) {
                    WaitToConfirmMoveState waitToConfirmMoveState = new WaitToConfirmMoveState(getBoard());
                    waitToConfirmMoveState.setCoordsToCellListeners(coordsToCellListeners);
                    waitToConfirmMoveState.setFrom(pieceToMoveCoords);
                    waitToConfirmMoveState.initState(coords);

                    getBoard().getBoardState().changeState(waitToConfirmMoveState);
                } else {
                    getBoard().getPlayerListener().onMakeMove(
                            acquiredCell.getCellViewModel().getModel().getCoords(),
                            cell.getCellViewModel().getModel().getCoords());
                    changeState(new WaitForOpponentMoveState(getBoard()));
                }
            } else { // move is not possible
                changeState(new WaitForSelectedPieceState(getBoard()));
            }
        } else { // it's not the right cell to construct a move
            changeState(new WaitForSelectedPieceState(getBoard()));
        }

        // common for both cell states
        acquiredCellListener.onUpdateFromBoard(CellModel.State.RELEASED);
    }

    @Override
    public void doOnUpdateFromPlayer() {
        // do nothing
    }

    @Override
    public void doOnUpdateFromChoosePiecePopup(Piece piece) {
        // do nothing
    }

    @Override
    public void beforeStateChanged() {
        // do nothing
    }

    @Override
    protected boolean isStateReturnedAfterHistory() {
        return false;
    }
}
