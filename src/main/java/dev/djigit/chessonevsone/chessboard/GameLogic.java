package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.piece.Piece;

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
}
