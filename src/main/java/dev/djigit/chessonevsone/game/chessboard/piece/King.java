package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Player.Color pieceColor, ImageView imageView) {
        super(pieceColor, imageView);
    }

    @Override
    public List<CellModel.Coords> getMoves(CellModel.Coords from) {
        List<CellModel.Coords> toCoords = new ArrayList<>();

        // →
        if (from.getX() + 1 <= 'h')
            toCoords.add(from.getByCoords((short) 1, (short) 0));
        // ↗
        if (from.getX() + 1 <= 'h' && from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) 1, (short) 1));
        // ↘
        if (from.getX() + 1 <= 'h' && from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) 1, (short) -1));

        // ←
        if (from.getX() - 1 >= 'a')
            toCoords.add(from.getByCoords((short) -1, (short) 0));
        // ↖
        if (from.getX() - 1 >= 'a' && from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) -1, (short) 1));
        // ↙
        if (from.getX() - 1 >= 'a' && from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) -1, (short) -1));

        // ↑
        if (from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) 0, (short) 1));
        // ↓
        if (from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) 0, (short) -1));

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
