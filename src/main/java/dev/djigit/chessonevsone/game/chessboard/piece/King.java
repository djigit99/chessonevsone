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
        // →→ (castling)
        if ((getPieceColor().isWhite() && from.equals(CellModel.Coords.E1)) ||
                (!getPieceColor().isWhite() && from.equals(CellModel.Coords.E8)))
            toCoords.add(from.getByCoords((short) 2, (short) 0));

        // ↗
        if (from.getX() + 1 <= 'h' && from.getY() + 1 <= 8)
            toCoords.add(from.getByCoords((short) 1, (short) 1));
        // ↘
        if (from.getX() + 1 <= 'h' && from.getY() - 1 >= 1)
            toCoords.add(from.getByCoords((short) 1, (short) -1));

        // ←
        if (from.getX() - 1 >= 'a')
            toCoords.add(from.getByCoords((short) -1, (short) 0));
        // ←← (castling)
        if ((getPieceColor().isWhite() && from.equals(CellModel.Coords.E1)) ||
                (!getPieceColor().isWhite() && from.equals(CellModel.Coords.E8)))
            toCoords.add(from.getByCoords((short) -2, (short) 0));
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
        boolean isCastling = isCastling(from, to);

        if (!isCastling) {
            return new CellModel.Coords[]{from, to};
        }

        CellModel.Coords[] cellPath;

        if (to.getX() > from.getX()) { //short castling
            cellPath = new CellModel.Coords[4];
            cellPath[0] = from;
            cellPath[1] = from.getByCoords((short) 1, (short) 0);
            cellPath[2] = from.getByCoords((short) 2, (short) 0);
            cellPath[3] = from.getByCoords((short) 3, (short) 0); // rook coords
        } else { // long castling
            cellPath = new CellModel.Coords[5];
            cellPath[0] = from;
            cellPath[1] = from.getByCoords((short) -1, (short) 0);
            cellPath[2] = from.getByCoords((short) -2, (short) 0);
            cellPath[3] = from.getByCoords((short) -3, (short) 0);
            cellPath[4] = from.getByCoords((short) -4, (short) 0); // rook coords
        }

        return cellPath;
    }

    @Override
    public boolean isMovePossible(Cell[] cells) {
        CellModel.Coords from = cells[0].getCellViewModel().getModel().getCoords();
        CellModel.Coords to = cells[cells.length-1].getCellViewModel().getModel().getCoords();

        boolean isCastling = isCastling(from, to);

        if (isCastling) {
            if (!(cells[cells.length-1].hasPiece() && cells[cells.length-1].getPiece() instanceof Rook))
                return false; // no rook

            // if any piece has got a move
            Rook rook = (Rook) cells[cells.length-1].getPiece();
            if (rook.getLastMove().isPresent() || cells[0].getPiece().getLastMove().isPresent())
                return false;

            // no piece between the king and rook
            for (int i = 1; i < cells.length-1; i++) {
                if (cells[i].hasPiece())
                    return false;
            }

            return true;
        } else {
            return !cells[1].hasPiece() || !cells[1].isFriendPiece(getPieceColor());
        }
    }

    @Override
    public String getName() {
        return "king";
    }

    public static boolean isCastling(CellModel.Coords from, CellModel.Coords to) {
        return Math.abs(from.getX() - to.getX()) > 1;
    }
}
