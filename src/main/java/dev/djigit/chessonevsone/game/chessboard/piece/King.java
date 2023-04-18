package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardModel;
import dev.djigit.chessonevsone.game.chessboard.GameLogic;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.scene.image.ImageView;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public boolean isMovePossible(LinkedMap<CellModel.Coords, Piece> path) {
        CellModel.Coords from = path.firstKey();
        CellModel.Coords to = path.lastKey();

        boolean isCastling = isCastling(from, to);

        if (isCastling) {
            if (!(path.getValue(path.size()-1) != null && path.getValue(path.size()-1) instanceof Rook))
                return false; // no rook

            // if any piece has got a move
            Rook rook = (Rook) path.getValue(path.size()-1);
            if (rook.getLastMove().isPresent() || path.getValue(0).getLastMove().isPresent())
                return false;

            // no piece between the king and rook
            for (int i = 1; i < path.size()-1; i++) {
                if (path.getValue(i) != null)
                    return false;
            }

            return true;
        } else {
            return path.getValue(1) == null || !path.getValue(1).getPieceColor().equals(getPieceColor());
        }
    }

    @Override
    public String getName() {
        return "king";
    }

    @Override
    public boolean canAttack(LinkedMap<CellModel.Coords, Piece> path) {
        return path.size() == 2;
    }

    public static boolean isCastling(CellModel.Coords from, CellModel.Coords to) {
        return Math.abs(from.getX() - to.getX()) > 1;
    }

    public boolean isKingUnderCheck(CellModel.Coords kingCoords,
                                    Map<CellModel.Coords, Piece> coordsOpponentsPiece,
                                    ChessBoardModel chessBoardModel) {
        for (Map.Entry<CellModel.Coords, Piece> coordsPiecePair : coordsOpponentsPiece.entrySet()) {
            CellModel.Coords opPieceCoords = coordsPiecePair.getKey();
            Piece opPiece = coordsPiecePair.getValue();

            if (GameLogic.doesPieceAttack(opPiece, opPieceCoords, kingCoords, chessBoardModel)) return true;
        }
        return false;
    }
}
