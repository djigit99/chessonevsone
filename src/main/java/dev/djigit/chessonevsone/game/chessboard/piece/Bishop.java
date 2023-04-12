package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        for (short x = 1; x <= 8; x++) {
            // ↗
            char xTo1 = (char) (from.getX() + x);
            short yTo1 = (short) (from.getY() + x);
            if (xTo1 <= 'h' && yTo1 <= 8)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo1, yTo1));

            // ↘
            char xTo2 = (char) (from.getX() + x);
            short yTo2 = (short) (from.getY() - x);
            if (xTo2 <= 'h' && yTo2 >= 1)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo2, yTo2));

            // ↖
            char xTo3 = (char) (from.getX() - x);
            short yTo3 = (short) (from.getY() + x);
            if (xTo3 >= 'a' && yTo3 <= 8)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo3, yTo3));

            // ↙
            char xTo4 = (char) (from.getX() - x);
            short yTo4 = (short) (from.getY() - x);
            if (xTo4 >= 'a' && yTo4 >= 1)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo4, yTo4));
        }

        return toCoords;
    }

    @Override
    public CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to) {
        char x;
        short y;
        int i;
        final int length = Math.abs(from.getY() - to.getY()) + 1;
        CellModel.Coords[] coords = new CellModel.Coords[length];

        if (from.getX() < to.getX()) {
            if (from.getY() < to.getY()) { // ↗
                for (i=0,x=from.getX(),y=from.getY(); i < length; i++,x++,y++)
                    coords[i] = CellModel.Coords.getCordsByValue(x, y);
            } else { // ↘
                for (i=0,x=from.getX(),y=from.getY(); i < length; i++,x++,y--)
                    coords[i] = CellModel.Coords.getCordsByValue(x, y);
            }
        } else {
            if (from.getY() < to.getY()) { // ↖
                for (i=0,x=from.getX(),y=from.getY(); i < length; i++,x--,y++)
                    coords[i] = CellModel.Coords.getCordsByValue(x, y);
            } else { // ↙
                for (i=0,x=from.getX(),y=from.getY(); i < length; i++,x--,y--)
                    coords[i] = CellModel.Coords.getCordsByValue(x, y);
            }
        }
        return coords;
    }

    @Override
    public boolean isMovePossible(Cell[] cells) {
        for (int i = 1; i < cells.length - 1; i++) {
            if (cells[i].hasPiece())
                return false;
        }

        return !cells[cells.length-1].hasPiece() || !cells[cells.length-1].isFriendPiece(getPieceColor());
    }

    @Override
    public String getName() {
        return "bishop";
    }

    public static Bishop createBrandNewBishop(Player.Color pieceColor) {
        final String WHITE_BISHOP_URL = "/pieces/queen_w.png";
        final String BLACK_BISHOP_URL = "/pieces/queen_b.png";
        final double BISHOP_FIT_WIDTH = 82;
        final double BISHOP_FIT_HEIGHT = 82;
        final double BISHOP_X_LAYOUT = 1;
        final double BISHOP_Y_LAYOUT = 3;

        Image pieceImage = new Image(pieceColor.isWhite() ? WHITE_BISHOP_URL : BLACK_BISHOP_URL);
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(BISHOP_FIT_WIDTH);
        imageView.setFitHeight(BISHOP_FIT_HEIGHT);
        imageView.setLayoutX(BISHOP_X_LAYOUT);
        imageView.setLayoutY(BISHOP_Y_LAYOUT);

        return new Bishop(pieceColor, imageView);
    }
}
