package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        for (char x = 'a'; x <= 'h'; x++) {
            if (x != from.getX())
                toCoords.add(CellModel.Coords.getCordsByValue(x, from.getY()));
        }

        for (short y = 1; y <= 8; y++) {
            if (y != from.getY())
                toCoords.add(CellModel.Coords.getCordsByValue(from.getX(), y));
        }

        return toCoords;
    }

    @Override
    public CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to) {
        if (from.getX() == to.getX()) {
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
        } else {
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
