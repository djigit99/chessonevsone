package dev.djigit.chessonevsone.game.chessboard.popup;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardListener;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.piece.Bishop;
import dev.djigit.chessonevsone.game.chessboard.piece.Knight;
import dev.djigit.chessonevsone.game.chessboard.piece.Queen;
import dev.djigit.chessonevsone.game.chessboard.piece.Rook;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ChoosePiecePopup {
    private Popup popup;
    private final ChessBoardListener chessBoardListener;
    private Cell ownerCell;
    private ChoosePiecePoputAbstractController choosePiecePopupController;

    public ChoosePiecePopup(ChessBoardListener chessBoardListener) {
        this.chessBoardListener = chessBoardListener;
    }

    public ChoosePiecePopup buildPopup(Player.Color pieceColor, Cell ownerCell) {
        final String CHOOSE_PIECE_WHITE_URL = "/scenes/ChoosePieceWhite.fxml";
        final String CHOOSE_PIECE_BLACK_URL = "/scenes/ChoosePieceBlack.fxml";
        URL choosePieceUrl;

        if (pieceColor.isWhite()) {
            choosePieceUrl = getClass().getResource(CHOOSE_PIECE_WHITE_URL);
        } else {
            choosePieceUrl = getClass().getResource(CHOOSE_PIECE_BLACK_URL);
        }

        FXMLLoader choosePieceLoader = FXMLLoaderFactory.getFXMLLoader(choosePieceUrl);
        Parent choosePieceParent;
        try {
            choosePieceParent = choosePieceLoader.load();
            choosePiecePopupController = choosePieceLoader.getController();
        } catch (IOException e) {
            System.out.println("Can't load popup");
            throw new RuntimeException(e);
        }

        Popup chooseColorPopup = new Popup();
        chooseColorPopup.getContent().add(choosePieceParent);

        Pane cellPane = ownerCell.getPane();
        Bounds boundsInLocal = cellPane.getBoundsInLocal();
        Bounds bounds = cellPane.localToScreen(boundsInLocal);
        chooseColorPopup.setX(bounds.getMinX() + cellPane.getWidth());
        chooseColorPopup.setY(bounds.getMinY());

        this.popup = chooseColorPopup;
        this.ownerCell = ownerCell;

        initPieces();

        return this;
    }

    private void initPieces() {

        List<ChoosePiecePoputAbstractController.PaneAndName> paneAndNames = choosePiecePopupController.getPieces();
        paneAndNames.forEach(pan -> {
            Pane piecePane = pan.getPane();
            String[] pieceNameAndColor = pan.getName().split("_");
            String pieceName = pieceNameAndColor[0];
            Player.Color pieceColor = pieceNameAndColor[1].equals("w") ? Player.Color.WHITE : Player.Color.BLACK;

            setHandlerForPiecePane(piecePane, pieceName, pieceColor);
        });

    }

    private void setHandlerForPiecePane(Pane piecePane, String pieceName, Player.Color pieceColor) {
        if ("queen".equals(pieceName)) {
            piecePane.addEventHandler(MouseEvent.MOUSE_CLICKED, eh -> {
                chessBoardListener.onUpdateFromPawnTransformToPiecePopup(Queen.createBrandNewQueen(pieceColor));
                hide();
            });
        }
        else if ("bishop".equals(pieceName)) {
            piecePane.addEventHandler(MouseEvent.MOUSE_CLICKED, eh -> {
                chessBoardListener.onUpdateFromPawnTransformToPiecePopup(Bishop.createBrandNewBishop(pieceColor));
                hide();
            });
        }
        else if ("knight".equals(pieceName)) {
            piecePane.addEventHandler(MouseEvent.MOUSE_CLICKED, eh -> {
                chessBoardListener.onUpdateFromPawnTransformToPiecePopup(Knight.createBrandNewKnight(pieceColor));
                hide();
            });
        }
        else if ("rook".equals(pieceName)) {
            piecePane.addEventHandler(MouseEvent.MOUSE_CLICKED, eh -> {
                chessBoardListener.onUpdateFromPawnTransformToPiecePopup(Rook.createBrandNewRook(pieceColor));
                hide();
            });
        }
        else
            System.out.println("Unknown piece to be loaded");
    }

    public void hide() {
        popup.hide();
    }

    public void showPopup() {
        popup.show(ownerCell.getPane().getScene().getWindow());
        popup.setX(Double.NaN); // not to update X-cord when the style of ChoosePiece popup's elements changed
        popup.setY(Double.NaN); // not to update Y-cord when the style of ChoosePiece popup's elements changed
    }

}
