package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        if (getPieceColor().isWhite()) {
            if (from.getY() + 1 <= 8) {
                toCoords.add(from.getByCoords((short) 0, (short) 1));
            }
            if (from.getY() == 2) {
                toCoords.add(from.getByCoords((short) 0, (short) 2));
            }
            if (from.getX() + 1 <= 'h' && from.getY() + 1 <= 8) {
                toCoords.add(from.getByCoords((short) 1, (short) 1));
            }
            if (from.getX() - 1 >= 'a' && from.getY() + 1 <= 8) {
                toCoords.add(from.getByCoords((short) -1, (short) 1));
            }
        } else {
            if (from.getY() - 1 >= 1) {
                toCoords.add(from.getByCoords((short) 0, (short) -1));
            }
            if (from.getY() == 7) {
                toCoords.add(from.getByCoords((short) 0, (short) -2));
            }
            if (from.getX() + 1 <= 'h' && from.getY() -1 >= 1) {
                toCoords.add(from.getByCoords((short) 1, (short) -1));
            }
            if (from.getX() - 1 >= 'a' && from.getY() - 1 >= 1) {
                toCoords.add(from.getByCoords((short) -1, (short) -1));
            }
        }

        return toCoords;
    }

    @Override
    public CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to) {
        CellModel.Coords[] path = new CellModel.Coords[Math.abs(to.getY() - from.getY()) + 1];
        if (from.getX() == to.getX()) {
            if (to.getY() > from.getY()) { // is while to move
                for (short y = from.getY(), i = 0; y <= to.getY(); y++, i++) {
                    path[i] = CellModel.Coords.getCordsByValue(from.getX(), y);
                }
            } else { // is black to move
                for (short y = from.getY(), i = 0; y >= to.getY(); y--, i++) {
                    path[i] = CellModel.Coords.getCordsByValue(from.getX(), y);
                }
            }
        } else {
            path[0] = from;
            path[1] = to;
        }
        return path;
    }

    @Override
    public boolean isMovePossible(Cell[] cells) {
        final int length = cells.length;
        CellModel.Coords from = cells[0].getCellViewModel().getModel().getCoords();
        CellModel.Coords to = cells[length-1].getCellViewModel().getModel().getCoords();

        if (from.getX() == to.getX()) {
            for (int i = 1; i <= length - 1; i++) {
                if (cells[i].hasPiece())
                    return false;
            }
        } else {
            return cells[length-1].hasPiece() && !cells[length-1].isFriendPiece(getPieceColor());
        }
        return true;
    }
}
