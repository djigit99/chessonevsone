package dev.djigit.chessonevsone.game.chessboard.cell;

import dev.djigit.chessonevsone.game.chessboard.ChessBoardListener;

public class CellListener {
    private CellViewModel cellViewModel;
    private ChessBoardListener chessBoardListener;

    public CellListener(CellViewModel cellViewModel) {
        this.cellViewModel = cellViewModel;
    }

    public void setChessBoardListener(ChessBoardListener chessBoardListener) {
        this.chessBoardListener = chessBoardListener;
    }

    public void onUpdateFromBoard(CellModel.State newState) {
        cellViewModel.setState(newState);
    }

    public void onUpdateFromPane() {
        CellModel model = cellViewModel.getModel();
        chessBoardListener.onUpdateReceived(model.getCoords());
    }


}
