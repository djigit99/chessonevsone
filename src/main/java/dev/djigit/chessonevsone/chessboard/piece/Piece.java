package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;


import java.util.List;

public abstract class Piece {
    private Player.Color pieceColor;
    private ImageView imageView;

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

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public abstract List<CellModel.Coords> getMoves(CellModel.Coords from);
    public abstract CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to);
    public abstract boolean isMovePossible(Cell[] cells);
}
