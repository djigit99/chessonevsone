package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
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
                boolean itNeedsPostMake = board.makeMove(from, to);

                if (itNeedsPostMake && isPawnTransformationReceived(coords)) {
                    String pieceName = coords[2];
                    Piece piece;
                    if ("queen".equals(pieceName))
                        piece = Queen.createBrandNewQueen(getOpponentsColor());
                    else if ("bishop".equals(pieceName))
                        piece = Bishop.createBrandNewBishop(getOpponentsColor());
                    else if ("knight".equals(pieceName))
                        piece = Knight.createBrandNewKnight(getOpponentsColor());
                    else if ("rook".equals(pieceName))
                        piece = Rook.createBrandNewRook(getOpponentsColor());
                    else
                        throw new IllegalStateException("Unknown piece received: " + pieceName);

                    board.doPostMakeMove(piece, to);
                }

                board.getBoardState().setCoordsToCellListeners(coordsToCellListeners);
                board.getBoardState().doOnUpdateFromPlayer();
            });
        }
    }

    private boolean isPawnTransformationReceived(String[] coords) {
        return coords.length > 2;
    }

    private Player.Color getOpponentsColor() {
        return board.getPlayerColor().isWhite() ? Player.Color.BLACK : Player.Color.WHITE;
    }

    public void onUpdateFromPawnTransformToPiecePopup(Piece piece) {
        board.getBoardState().doOnUpdateFromChoosePiecePopup(piece);
    }
}
