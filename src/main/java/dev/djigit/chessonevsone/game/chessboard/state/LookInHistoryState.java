package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class LookInHistoryState extends ChessBoard.ChessBoardState {

    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public LookInHistoryState(ChessBoard board) {
        super(board);
    }

    @Override
    public void doOnUpdateFromCell(CellModel.Coords coords) {
        // do nothing
    }

    @Override
    public void doOnUpdateFromPlayer() {
        GameHistory history = getBoard().getGameHistory();
        getBoard().restoreSnapshot(history.changeToLatest());
        changeState(new WaitForSelectedPieceState(getBoard()));

        coordsToCellListeners.values()
                .forEach(ccL -> ccL.getRight().onUpdateFromBoard(CellModel.State.RELEASED));
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        this.coordsToCellListeners = coordsToCellListeners;
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

    @Override
    public void beforeStateChangedToHistory() {
        throw new IllegalStateException("The history state can not be change to a history state.");
    }
}
