package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.King;
import dev.djigit.chessonevsone.game.chessboard.piece.Pawn;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Map;

public class GameLogic {
    private final ChessBoardModel chessBoardModel;

    public GameLogic(ChessBoardModel chessBoardModel) {
        this.chessBoardModel = chessBoardModel;
    }

    public boolean isMovePossible(Piece piece, CellModel.Coords from, CellModel.Coords to) {
        List<CellModel.Coords> moves = piece.getMoves(from);
        if (!moves.contains(to))
            return false;

        CellModel.Coords[] path = piece.getPath(from, to);

        LinkedMap<CellModel.Coords, Piece> piecesOnPath = chessBoardModel.getPiecesByCoords(path);

        if (!piece.isMovePossible(piecesOnPath)) {
            return false;
        }

        ChessBoardModel boardModelAfterMove = chessBoardModel.clone();
        boardModelAfterMove.transferPiece(from, to);

        // 'remove' opponent's pawn after en passant
        if (piece instanceof Pawn && ((Pawn) piece).isLastMoveEnPassant()) {
            if (piece.getPieceColor().isWhite()) {
                boardModelAfterMove.cleanPiece(to.getByCoords((short) 0, (short) -1));
            } else {
                boardModelAfterMove.cleanPiece(to.getByCoords((short) 0, (short) 1));
            }
        }

        if (piece instanceof King && King.isCastling(from, to)) {
            boolean canKingGoCastling = !isKingUnderCheckWhenCasting(chessBoardModel, (King) piece, from, to);
            if (!canKingGoCastling)
                return false;
        }

        ImmutablePair<CellModel.Coords, King> coordsKingPair = boardModelAfterMove.getKing(piece.getPieceColor());
        CellModel.Coords kingCoords = coordsKingPair.getLeft();
        King king = coordsKingPair.getRight();
        Map<CellModel.Coords, Piece> opponentsPieces = boardModelAfterMove.getOpponentsPieces(piece.getPieceColor());

        return !king.isKingUnderCheck(kingCoords, opponentsPieces, boardModelAfterMove);
    }

    /**
     * Check if the piece making the move is a pawn, and if yes then
     * check if the current move is the 'en passant' move.
     * <p>
     * This method marks the last move as 'en passant' for Pawn object.
     *
     * @param from from position.
     * @param to to position.
     *
     * @return true - if the move is 'en passant'.
    * */
    public boolean isMoveEnPassant(CellModel.Coords from, CellModel.Coords to) {
        LinkedMap<CellModel.Coords, Piece> piecesOnPath = chessBoardModel.getPiecesByCoords(new CellModel.Coords[] {from, to});
        Piece piece = piecesOnPath.getValue(0);

        if (piece instanceof Pawn && from.getX() != to.getX() && piecesOnPath.getValue(1) == null) {
            ((Pawn) piece).markLastMoveEnPassant();
            return true;
        }

        return false;
    }

    public static boolean isTheLastRawForPawn(CellModel.Coords to, Piece piece) {
        final int WHITE_LAST_ROW = 8;
        final int BLACK_LAST_ROW = 1;

        return piece instanceof Pawn &&
                (
                    (piece.getPieceColor().isWhite() && to.getY() == WHITE_LAST_ROW) ||
                    (!piece.getPieceColor().isWhite() && to.getY() == BLACK_LAST_ROW)
                );
    }

    public static boolean isKingUnderCheckWhenCasting(
            ChessBoardModel chessBoardModel,
            King king, CellModel.Coords kingCoords, CellModel.Coords to) {
        Map<CellModel.Coords, Piece> opponentsPieces = chessBoardModel.getOpponentsPieces(king.getPieceColor());

        if (king.isKingUnderCheck(kingCoords, opponentsPieces, chessBoardModel))
            return true;

        short whereKingGoesWhenCastling = (short) (to.getX() > kingCoords.getX() ? 1 : -1);
        ChessBoardModel afterKingLeft = chessBoardModel.clone();
        afterKingLeft.transferPiece(kingCoords, kingCoords.getByCoords(whereKingGoesWhenCastling, (short) 0));

        return king.isKingUnderCheck(kingCoords.getByCoords(whereKingGoesWhenCastling, (short) 0), opponentsPieces, afterKingLeft);
    }

    public static boolean doesPieceAttack(Piece piece,
                                   CellModel.Coords pieceCoords,
                                   CellModel.Coords attackingCoords,
                                   ChessBoardModel chessBoardModel) {
        List<CellModel.Coords> moves = piece.getMoves(pieceCoords);
        if (!moves.contains(attackingCoords))
            return false;

        CellModel.Coords[] path = piece.getPath(pieceCoords, attackingCoords);

        LinkedMap<CellModel.Coords, Piece> piecesOnPath = chessBoardModel.getPiecesByCoords(path);

        return piece.canAttack(piecesOnPath);
    }
}
