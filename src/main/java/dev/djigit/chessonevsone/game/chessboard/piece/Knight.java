package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.collections4.map.LinkedMap;

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
    public boolean isMovePossible(LinkedMap<CellModel.Coords, Piece> path) {
        return path.getValue(path.size()-1) == null ||
                !path.getValue(path.size()-1).getPieceColor().equals(getPieceColor());
    }

    @Override
    public String getName() {
        return "knight";
    }

    @Override
    public boolean canAttack(LinkedMap<CellModel.Coords, Piece> path) {
        return true;
    }

    public static Knight createBrandNewKnight(Player.Color pieceColor) {
        final String WHITE_KNIGHT_URL = "/pieces/knight_w.png";
        final String BLACK_KNIGHT_URL = "/pieces/knight_b.png";
        final double KNIGHT_FIT_WIDTH = 82;
        final double KNIGHT_FIT_HEIGHT = 82;
        final double KNIGHT_X_LAYOUT = 1;
        final double KNIGHT_Y_LAYOUT = 3;

        Image pieceImage = new Image(pieceColor.isWhite() ? WHITE_KNIGHT_URL : BLACK_KNIGHT_URL);
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(KNIGHT_FIT_WIDTH);
        imageView.setFitHeight(KNIGHT_FIT_HEIGHT);
        imageView.setLayoutX(KNIGHT_X_LAYOUT);
        imageView.setLayoutY(KNIGHT_Y_LAYOUT);

        return new Knight(pieceColor, imageView);
    }
}
