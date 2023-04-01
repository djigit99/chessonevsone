package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        // one right, two above
        if (from.getX() + 1 <= 'h' && from.getY() + 2 <= 8)
            toCoords.add(from.getByCoords((short) 1, (short) 2));

        // two right, one above
        if (from.getX() + 2 <= 'h' && from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) 2,(short) 1));

        // two right, one below
        if (from.getX() + 2 <= 'h' && from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) 2, (short) -1));

        // one right, two below
        if (from.getX() + 1 <= 'h' && from.getY() - 2 >= 1)
            toCoords.add(from.getByCoords((short) 1, (short) -2));

        // one left, two above
        if (from.getX() - 1 >= 'a' && from.getY() + 2 <= 8)
            toCoords.add(from.getByCoords((short) -1, (short) 2));

        // two left, one above
        if (from.getX() - 2 >= 'a' && from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) -2, (short) 1));

        // two left, one below
        if (from.getX() - 2 >= 'a' && from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) -2, (short) -1));

        // one left, two below
        if (from.getX() - 1 >= 'a' && from.getY() - 2 >= 1)
            toCoords.add(from.getByCoords((short) -1, (short) -2));

        return toCoords;
    }

    @Override
    public CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to) {
        return new CellModel.Coords[]{from, to};
    }

    @Override
    public boolean isMovePossible(Cell[] cells) {
        return !cells[cells.length-1].hasPiece() ||
                !cells[cells.length-1].isFriendPiece(getPieceColor());
    }
}
