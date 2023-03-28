package dev.djigit.chessonevsone.chessboard.state;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public abstract class ChessBoardState {
    private ChessBoard board;

    public ChessBoardState(ChessBoard board) {
        this.board = board;
    }

    public abstract void doOnUpdate(CellModel.Coords coords);

    ChessBoard getBoard() {
        return board;
    }

    public abstract void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners);
    public abstract void setCellState(CellModel.State cellState);
}
