package dev.djigit.chessonevsone.game.chessboard.history;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;

import java.util.Map;

public class ChessBoardSnapshot {
    private final Map<CellModel.Coords, Piece> coordsToPiece;

    public ChessBoardSnapshot(Map<CellModel.Coords, Piece> coordsToPiece) {
        this.coordsToPiece = coordsToPiece;
    }

    public Map<CellModel.Coords, Piece> getState() {
        return coordsToPiece;
    }
}
