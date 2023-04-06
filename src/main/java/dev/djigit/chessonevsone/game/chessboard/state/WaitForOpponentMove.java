package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitForOpponentMove extends ChessBoardState {

    public WaitForOpponentMove(ChessBoard board) {
        super(board);
    }

    @Override
    public void doOnUpdate(CellModel.Coords coords) {
        // do nothing
    }

    @Override
    public void doOnUpdateFromPlayer() {
        getBoard().changeState(new WaitForSelectedPieceState(getBoard()));
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        // do nothing
    }
}
