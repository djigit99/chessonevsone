package dev.djigit.chessonevsone.game.chessboard.state;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import dev.djigit.chessonevsone.game.chessboard.popup.ChoosePiecePopup;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Map;

public class WaitToConfirmMoveState extends ChessBoard.ChessBoardState {

    private CellModel.Coords from;
    private CellModel.Coords to;
    private ChoosePiecePopup popup;
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners;

    public WaitToConfirmMoveState(ChessBoard board) {
        super(board);
    }

    @Override
    public void doOnUpdateFromCell(CellModel.Coords coords) {
        // do nothing
    }

    @Override
    public void doOnUpdateFromPlayer() {
        // do nothing
    }

    @Override
    public void doOnUpdateFromChoosePiecePopup(Piece piece) {
        CellListener cellListener = coordsToCellListeners.get(to).getRight();

        getBoard().doPostMakeMove(piece, to);

        getBoard().getPlayerListener().onMakeMove(
                from,
                to,
                piece);

        cellListener.onUpdateFromBoard(CellModel.State.RELEASED);
        changeState(new WaitForOpponentMoveState(getBoard()));
    }

    @Override
    public void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners) {
        this.coordsToCellListeners = coordsToCellListeners;
    }

    public void setFrom(CellModel.Coords from) {
        this.from = from;
    }

    @Override
    public void beforeStateChanged() {
        CellListener cellListener = coordsToCellListeners.get(to).getRight();
        cellListener.onUpdateFromBoard(CellModel.State.RELEASED);

        this.popup.hide();
    }

    @Override
    protected boolean isStateReturnedAfterHistory() {
        return false;
    }

    void initState(CellModel.Coords to) {
        this.to = to;

        Cell cell = coordsToCellListeners.get(to).getLeft();
        CellListener cellListener = coordsToCellListeners.get(to).getRight();

        cellListener.onUpdateFromBoard(CellModel.State.ACQUIRED); // todo: think about to add a new cell state

        this.popup = getBoard().showChoosePiecePopup(cell);
    }
}
