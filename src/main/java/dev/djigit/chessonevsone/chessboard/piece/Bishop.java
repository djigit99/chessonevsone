package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Player.Color pieceColor, ImageView imageView, CellModel.Coords initialCoords) {
        super(pieceColor, imageView, initialCoords);
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
}
