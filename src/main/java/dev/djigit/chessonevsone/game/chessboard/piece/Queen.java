package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

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
    public boolean isMovePossible(Cell[] cells) {
        for (int i = 1; i < cells.length - 1; i++) {
            if (cells[i].hasPiece())
                return false;
        }

        return !cells[cells.length-1].hasPiece() || !cells[cells.length-1].isFriendPiece(getPieceColor());
    }
}
