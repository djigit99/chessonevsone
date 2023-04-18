package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        // as Rook
        for (char x = 'a'; x <= 'h'; x++) {
            if (x != from.getX())
                toCoords.add(CellModel.Coords.getCordsByValue(x, from.getY()));
        }

        for (short y = 1; y <= 8; y++) {
            if (y != from.getY())
                toCoords.add(CellModel.Coords.getCordsByValue(from.getX(), y));
        }

        // as Bishop
        for (short x = 1; x <= 8; x++) {
            char xTo1 = (char) (from.getX() + x);
            short yTo1 = (short) (from.getY() + x);
            if (xTo1 <= 'h' && yTo1 <= 8)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo1, yTo1));

            char xTo2 = (char) (from.getX() + x);
            short yTo2 = (short) (from.getY() - x);
            if (xTo2 <= 'h' && yTo2 >= 1)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo2, yTo2));

            char xTo3 = (char) (from.getX() - x);
            short yTo3 = (short) (from.getY() + x);
            if (xTo3 >= 'a' && yTo3 <= 8)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo3, yTo3));

            char xTo4 = (char) (from.getX() - x);
            short yTo4 = (short) (from.getY() - x);
            if (xTo4 >= 'a' && yTo4 >= 1)
                toCoords.add(CellModel.Coords.getCordsByValue(xTo4, yTo4));
        }

        return toCoords;
    }

    @Override
    public CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to) {
        if (from.getX() == to.getX()) { // the same as Rook
            short minY = (short) Math.min(from.getY(), to.getY());
            short maxY = (short) Math.max(from.getY(), to.getY());

            final int length = maxY - minY + 1;
            CellModel.Coords[] coords = new CellModel.Coords[length];

            if (from.getY() < to.getY()) {
                for (short y = from.getY(), i = 0; y <= to.getY(); y++, i++) {
                    coords[i] = CellModel.Coords.getCordsByValue(from.getX(), y);
                }
            } else {
                for (short y = from.getY(), i = 0; y >= to.getY(); y--, i++) {
                    coords[i] = CellModel.Coords.getCordsByValue(from.getX(), y);
                }
            }

            return coords;
        } else if (from.getY() == to.getY()){
            char minX = (char) Math.min(from.getX(), to.getX());
            char maxX = (char) Math.max(from.getX(), to.getX());

            final int length = maxX - minX + 1;
            CellModel.Coords[] coords = new CellModel.Coords[length];

            if (from.getX() < to.getX()) {
                for (char x = from.getX(), i = 0; x <= to.getX(); x++, i++) {
                    coords[i] = CellModel.Coords.getCordsByValue(x, from.getY());
                }
            } else {
                for (char x = from.getX(), i = 0; x >= to.getX(); x--, i++) {
                    coords[i] = CellModel.Coords.getCordsByValue(x, from.getY());
                }
            }

            return coords;
        } else { // the same as Bishop
            char x;
            short y;
            int i;
            final int length = Math.abs(from.getY() - to.getY()) + 1;
            CellModel.Coords[] coords = new CellModel.Coords[length];

            if (from.getX() < to.getX()) {
                if (from.getY() < to.getY()) { // ↗
                    for (i = 0, x = from.getX(), y = from.getY(); i < length; i++, x++, y++)
                        coords[i] = CellModel.Coords.getCordsByValue(x, y);
                } else { // ↘
                    for (i = 0, x = from.getX(), y = from.getY(); i < length; i++, x++, y--)
                        coords[i] = CellModel.Coords.getCordsByValue(x, y);
                }
            } else {
                if (from.getY() < to.getY()) { // ↖
                    for (i = 0, x = from.getX(), y = from.getY(); i < length; i++, x--, y++)
                        coords[i] = CellModel.Coords.getCordsByValue(x, y);
                } else { // ↙
                    for (i = 0, x = from.getX(), y = from.getY(); i < length; i++, x--, y--)
                        coords[i] = CellModel.Coords.getCordsByValue(x, y);
                }
            }
            return coords;
        }
    }

    @Override
    public boolean isMovePossible(LinkedMap<CellModel.Coords, Piece> path) {
        final int length = path.size();

        for (int i = 1; i < length - 1; i++) {
            if (path.getValue(i) != null)
                return false;
        }

        return path.getValue(length-1) == null || !path.getValue(length-1).getPieceColor().equals(getPieceColor());
    }

    @Override
    public String getName() {
        return "queen";
    }

    @Override
    public boolean canAttack(LinkedMap<CellModel.Coords, Piece> path) {
        for (int i = 1; i < path.size() - 1; i++) {
            if (path.getValue(i) != null)
                return false;
        }
        return true;
    }

    public static Queen createBrandNewQueen(Player.Color pieceColor) {
        final String WHITE_QUEEN_URL = "/pieces/queen_w.png";
        final String BLACK_QUEEN_URL = "/pieces/queen_b.png";
        final double QUEEN_FIT_WIDTH = 90;
        final double QUEEN_FIT_HEIGHT = 90;
        final double QUEEN_X_LAYOUT = 0;
        final double QUEEN_Y_LAYOUT = 0;

        Image pieceImage = new Image(pieceColor.isWhite() ? WHITE_QUEEN_URL : BLACK_QUEEN_URL);
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(QUEEN_FIT_WIDTH);
        imageView.setFitHeight(QUEEN_FIT_HEIGHT);
        imageView.setLayoutX(QUEEN_X_LAYOUT);
        imageView.setLayoutY(QUEEN_Y_LAYOUT);

        return new Queen(pieceColor, imageView);
    }
}
