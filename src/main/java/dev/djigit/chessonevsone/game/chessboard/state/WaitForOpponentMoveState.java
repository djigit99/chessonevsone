package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitForOpponentMoveState extends ChessBoard.ChessBoardState {

    public WaitForOpponentMoveState(ChessBoard board) {
        super(board);
    }

    @Override
    public void doOnUpdate(CellModel.Coords coords) {
        // do nothing
    }

    @Override
    public void doOnUpdateFromPlayer() {
        changeState(new WaitForSelectedPieceState(getBoard()));
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        // do nothing
    }

    @Override
    public void doOnUpdateFromChoosePiecePopup(Piece piece) {
        // do nothing
    }
}
