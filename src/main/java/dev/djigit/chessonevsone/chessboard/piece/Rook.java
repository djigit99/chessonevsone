package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(Player.Color pieceColor, ImageView imageView, CellModel.Coords initialCoords) {
        super(pieceColor, imageView, initialCoords);
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
        throw new NotImplementedException();
    }

    @Override
    public boolean isMovePossible(Cell[] cells) {
        throw new NotImplementedException();
    }
}
