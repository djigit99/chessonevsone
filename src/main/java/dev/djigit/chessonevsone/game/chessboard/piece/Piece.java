package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;


import java.util.List;
import java.util.Optional;

public abstract class Piece {
    private Player.Color pieceColor;
    private ImageView imageView;
    private LastMove lastMove;

    public Piece(Player.Color pieceColor, ImageView imageView) {
        this.pieceColor = pieceColor;
        this.imageView = imageView;
    }

    public Player.Color getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(Player.Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setLastMove(LastMove lastMove) {
        this.lastMove = lastMove;
    }

    public Optional<LastMove> getLastMove() {
        if (lastMove == null)
            return Optional.empty();
        return Optional.of(lastMove);
    }

    public static class LastMove {
        private CellModel.Coords from;
        private CellModel.Coords to;

        public LastMove(CellModel.Coords from, CellModel.Coords to) {
            this.from = from;
            this.to = to;
        }

        public CellModel.Coords getFrom() {
            return from;
        }

        public CellModel.Coords getTo() {
            return to;
        }
    }

    public abstract List<CellModel.Coords> getMoves(CellModel.Coords from);
    public abstract CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to);
    public abstract boolean isMovePossible(Cell[] cells);
}
