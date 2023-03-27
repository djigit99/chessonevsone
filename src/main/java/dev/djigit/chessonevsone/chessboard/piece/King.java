package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Player.Color pieceColor, ImageView imageView, CellModel.Coords initialCoords) {
        super(pieceColor, imageView, initialCoords);
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
}
