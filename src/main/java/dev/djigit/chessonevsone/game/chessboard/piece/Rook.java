package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.collections4.map.LinkedMap;

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
        return "rook";
    }

    @Override
    public boolean canAttack(LinkedMap<CellModel.Coords, Piece> path) {
        for (int i = 1; i < path.size() - 1; i++) {
            if (path.getValue(i) != null)
                return false;
        }

        return true;
    }

    public static Rook createBrandNewRook(Player.Color pieceColor) {
        final String WHITE_ROOK_URL = "/pieces/rook_w.png";
        final String BLACK_ROOK_URL = "/pieces/rook_b.png";
        final double ROOK_FIT_WIDTH = 80;
        final double ROOK_FIT_HEIGHT = 80;
        final double ROOK_X_LAYOUT = 4;
        final double ROOK_Y_LAYOUT = 5;

        Image pieceImage = new Image(pieceColor.isWhite() ? WHITE_ROOK_URL : BLACK_ROOK_URL);
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(ROOK_FIT_WIDTH);
        imageView.setFitHeight(ROOK_FIT_HEIGHT);
        imageView.setLayoutX(ROOK_X_LAYOUT);
        imageView.setLayoutY(ROOK_Y_LAYOUT);

        return new Rook(pieceColor, imageView);
    }
}
