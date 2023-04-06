package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.state.ChessBoardState;
import dev.djigit.chessonevsone.sockets.MessageType;
import javafx.application.Platform;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class ChessBoardListener {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private ChessBoard board;

    public ChessBoardListener(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners, ChessBoard board) {
        this.coordsToCellListeners = coordsToCellListeners;
        this.board = board;
    }

    public void onUpdateFromCellReceived(CellModel.Coords coords) {
        ChessBoardState chessBoardState = board.getBoardState();
        chessBoardState.setCoordsToCellListeners(coordsToCellListeners);

        chessBoardState.doOnUpdate(coords);
    }

    public void onUpdateFromPlayerReceived(ImmutablePair<MessageType, String> msg) {
        MessageType msgType = msg.getLeft();

        if (msgType.equals(MessageType.OPP_MOVE)) {
            String[] coords = msg.getRight().split(" ");
            CellModel.Coords from = CellModel.Coords.valueOf(coords[0]);
            CellModel.Coords to = CellModel.Coords.valueOf(coords[1]);

            Platform.runLater(() -> {
                board.makeMove(from, to);
                board.getBoardState().doOnUpdateFromPlayer();
            });
        }
    }
}
