package dev.djigit.chessonevsone.game.chessboard.cell;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel.State;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import dev.djigit.chessonevsone.game.Player;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import static dev.djigit.chessonevsone.game.chessboard.cell.CellModel.Coords.getCordsByValue;

public class Cell {
    private final Pane cellPane;
    private CellViewModel cellViewModel;
    private final String initialStyle;
    private CellListener cellListener;
    private Piece piece;

    public Cell(Pane cellPane) {
        this.cellPane = cellPane;

        initialStyle = cellPane.getStyle();
        initWithData();
        addHandlers();
    }

    private void initWithData() {
        String paneId = cellPane.getId();
        String coord = paneId.substring(paneId.length() - 2);
        char coord_x = coord.charAt(0);
        short coord_y = (short) (coord.charAt(1) - '0');

        CellModel cellModel = new CellModel();
        cellModel.setCoords(getCordsByValue(coord_x, coord_y));
        cellModel.setState(State.RELEASED);

        cellViewModel = new CellViewModel(cellModel);
        cellListener = new CellListener(cellViewModel);
    }

    private void addHandlers() {
        cellViewModel.addStatePropertyListener(getChangeBorderListener());

        cellPane.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
            cellListener.onUpdateFromPane();
            ev.consume();
        });
    }

    private ChangeListener<CellModel.State> getChangeBorderListener() {
        final String RELEASED_STYLE = initialStyle;
        final String ACQUIRED_STYLE = initialStyle + "; -fx-border-width: 3px; -fx-border-color: red";
        return (observable, oldValue, newValue) -> {
            if (newValue.equals(State.ACQUIRED))
                cellPane.setStyle(ACQUIRED_STYLE);
            else
                cellPane.setStyle(RELEASED_STYLE);
        };
    }

    public boolean hasPiece() {
        return cellPane.getChildren().size() > 0;
    }

    public boolean isFriendPiece(Player.Color pretendedColor) {
        return piece.getPieceColor().equals(pretendedColor);
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece cleanPiece() {
        if (cellPane.getChildren().size() > 0) {
            cellPane.getChildren().remove(0);
        }
        Piece removedPiece = piece;
        piece = null;
        return removedPiece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (cellPane.getChildren().size() > 0)
            cellPane.getChildren().remove(0);

        if (piece != null && piece.getImageView() != null)
            cellPane.getChildren().add(piece.getImageView());
    }

    public CellListener getCellListener() {
        return cellListener;
    }

    public CellViewModel getCellViewModel() {
        return cellViewModel;
    }

    public Pane getPane() {
        return cellPane;
    }
}
