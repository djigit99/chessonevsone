package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public abstract class ChessBoardState {
    private ChessBoard board;

    public ChessBoardState(ChessBoard board) {
        this.board = board;
    }

    ChessBoard getBoard() {
        return board;
    }

    public abstract void doOnUpdate(CellModel.Coords coords);
    public abstract void doOnUpdateFromPlayer();

    public abstract void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners);
}
