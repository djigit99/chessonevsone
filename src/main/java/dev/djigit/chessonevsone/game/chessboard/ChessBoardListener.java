package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.state.ChessBoardState;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class ChessBoardListener {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private ChessBoard board;

    public ChessBoardListener(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners, ChessBoard board) {
        this.coordsToCellListeners = coordsToCellListeners;
        this.board = board;
    }

    public void onUpdateReceived(CellModel.Coords coords) {
        ChessBoardState chessBoardState = board.getBoardState();
        chessBoardState.setCoordsToCellListeners(coordsToCellListeners);

        chessBoardState.doOnUpdate(coords);
    }
}