package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class LookInHistoryState extends ChessBoard.ChessBoardState {

    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public LookInHistoryState(ChessBoard board) {
        super(board);
    }

    @Override
    public void doOnUpdate(CellModel.Coords coords) {
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
}
