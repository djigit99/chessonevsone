package dev.djigit.chessonevsone.game.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardModel;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.scene.image.ImageView;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.List;
import java.util.Optional;

public abstract class Piece {
    private Player.Color pieceColor;
    private final ImageView imageView;
    private LastMove lastMove;

    public Piece(Player.Color pieceColor, ImageView imageView) {
        this.pieceColor = pieceColor;
        this.imageView = imageView;
    }

    public Player.Color getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(Player.Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setLastMove(LastMove lastMove) {
        this.lastMove = lastMove;
    }

    public Optional<LastMove> getLastMove() {
        if (lastMove == null)
            return Optional.empty();
        return Optional.of(lastMove);
    }

    public static class LastMove {
        private final CellModel.Coords from;
        private final CellModel.Coords to;

        public LastMove(CellModel.Coords from, CellModel.Coords to) {
            this.from = from;
            this.to = to;
        }

        public CellModel.Coords getFrom() {
            return from;
        }

        public CellModel.Coords getTo() {
            return to;
        }
    }

    public abstract String getName();
    public abstract List<CellModel.Coords> getMoves(CellModel.Coords from);
    public abstract CellModel.Coords[] getPath(CellModel.Coords from, CellModel.Coords to);
    public abstract boolean isMovePossible(LinkedMap<CellModel.Coords, Piece> path);
    public boolean doesPieceAttack(CellModel.Coords pieceCoords,
                                   CellModel.Coords attackingCoords,
                                   ChessBoardModel chessBoardModel) {
        List<CellModel.Coords> moves = getMoves(pieceCoords);
        if (!moves.contains(attackingCoords))
            return false;

        CellModel.Coords[] path = getPath(pieceCoords, attackingCoords);

        LinkedMap<CellModel.Coords, Piece> piecesOnPath = chessBoardModel.getPiecesByCoords(path);

        return canAttack(piecesOnPath);
    }
    abstract boolean canAttack(LinkedMap<CellModel.Coords, Piece> path);
}
