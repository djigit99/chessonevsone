package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.sockets.MessageType;
import javafx.application.Platform;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class ChessBoardListener {
    private final Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;
    private final ChessBoard board;
    private final GameLogic gameLogic;

    public ChessBoardListener(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners,
                              ChessBoard board,
                              GameLogic gameLogic) {
        this.coordsToCellListeners = coordsToCellListeners;
        this.board = board;
        this.gameLogic = gameLogic;
    }

    public void onUpdateFromCellReceived(CellModel.Coords coords) {
        ChessBoard.ChessBoardState chessBoardState = board.getBoardState();
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
                gameLogic.isMoveEnPassant(from, to);
                board.makeMove(from, to);
                board.getBoardState().setCoordsToCellListeners(coordsToCellListeners);
                board.getBoardState().doOnUpdateFromPlayer();
            });
        }
    }
}
