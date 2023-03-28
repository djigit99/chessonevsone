package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.cell.CellModel.State;
import dev.djigit.chessonevsone.chessboard.state.ChessBoardState;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class ChessBoardListener {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private ChessBoard board;

    public ChessBoardListener(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners, ChessBoard board) {
        this.coordsToCellListeners = coordsToCellListeners;
        this.board = board;
    }

    public void onUpdateReceived(State cellState, CellModel.Coords coords) {
        ChessBoardState chessBoardState = board.getBoardState();
        chessBoardState.setCellState(cellState);
        chessBoardState.setCoordsToCellListeners(coordsToCellListeners);

        chessBoardState.doOnUpdate(coords);
    }
}
