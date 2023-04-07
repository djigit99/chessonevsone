package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Pawn;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;

import java.util.List;

public class GameLogic {
    private ChessBoard chessBoard;

    public GameLogic(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public boolean isMovePossible(Piece piece, CellModel.Coords from, CellModel.Coords to) {
        // todo: get piece path
        // todo: get all cells on path
        // todo: check if move is possible depending on the kind of the piece
        // todo: check if the king is under check

        List<CellModel.Coords> moves = piece.getMoves(from);
        if (!moves.contains(to))
            return false;

        CellModel.Coords[] path = piece.getPath(from, to);

        Cell[] cellsOnPath = chessBoard.getCellsOnPath(path);

        return piece.isMovePossible(cellsOnPath);
    }

    public boolean isMoveEnPassant(CellModel.Coords from, CellModel.Coords to) {
        Cell[] cellsOnPath = chessBoard.getCellsOnPath(new CellModel.Coords[] {from, to});
        Cell pieceCell = cellsOnPath[0];
        Cell toCell = cellsOnPath[1];
        Piece piece = pieceCell.getPiece();

        if (piece instanceof Pawn && from.getX() != to.getX() && !toCell.hasPiece()) {
            ((Pawn) piece).markLastMoveEnPassant();
            return true;
        }

        return false;
    }
}
