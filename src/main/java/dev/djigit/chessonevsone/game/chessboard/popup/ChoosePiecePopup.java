package dev.djigit.chessonevsone.game.chessboard.popup;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardListener;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
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

    private Queen queen;
    private Bishop bishop;
    private Knight knight;
    private Rook rook;
    private EventHandler<MouseEvent> queenEventHandler;
    private EventHandler<MouseEvent> bishopEventHandler;
    private EventHandler<MouseEvent> knightEventHandler;
    private EventHandler<MouseEvent> rookEventHandler;

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
        chooseColorPopup.setOnHidden(we -> {
            if (queen != null) {
                queen.getImageView().removeEventHandler(MouseEvent.MOUSE_CLICKED, queenEventHandler);
            }

            if (bishop != null) {
                bishop.getImageView().removeEventHandler(MouseEvent.MOUSE_CLICKED, bishopEventHandler);
            }

            if (knight != null) {
                knight.getImageView().removeEventHandler(MouseEvent.MOUSE_CLICKED, knightEventHandler);
            }

            if (rook != null) {
                rook.getImageView().removeEventHandler(MouseEvent.MOUSE_CLICKED, rookEventHandler);
            }

        });
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

        List<ChoosePieceWhitePopupController.ImageViewAndName> paneAndNames = choosePiecePopupController.getPieces();
        paneAndNames.forEach(pan -> {
            ImageView pieceImageView = pan.getImageView();
            String[] pieceNameAndColor = pan.getName().split("_");
            String pieceName = pieceNameAndColor[0];
            Player.Color pieceColor = pieceNameAndColor[1].equals("w") ? Player.Color.WHITE : Player.Color.BLACK;

            if ("queen".equals(pieceName)) {
                queen = new Queen(pieceColor, pieceImageView);
                queenEventHandler = eh -> {
                    chessBoardListener.onUpdateFromPawnTransformToPiecePopup(queen);
                    eh.consume();
                    hide();
                };

                pieceImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, queenEventHandler);
            }
            else if ("bishop".equals(pieceName)) {
                bishop = new Bishop(pieceColor, pieceImageView);
                bishopEventHandler = eh -> {
                    chessBoardListener.onUpdateFromPawnTransformToPiecePopup(bishop);
                    eh.consume();
                    hide();
                };

                pieceImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, bishopEventHandler);
            }
            else if ("knight".equals(pieceName)) {
                knight = new Knight(pieceColor, pieceImageView);
                knightEventHandler = eh -> {
                    chessBoardListener.onUpdateFromPawnTransformToPiecePopup(knight);
                    eh.consume();
                    hide();
                };

                pieceImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, knightEventHandler);
            }
            else if ("rook".equals(pieceName)) {
                rook = new Rook(pieceColor, pieceImageView);
                rookEventHandler = eh -> {
                    chessBoardListener.onUpdateFromPawnTransformToPiecePopup(rook);
                    eh.consume();
                    hide();
                };

                pieceImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, rookEventHandler);
            }
            else
                System.out.println("Unknown piece to be loaded");

        });

    }

    private void hide() {
        popup.hide();
    }

    public void showPopup() {
        popup.show(ownerCell.getPane().getScene().getWindow());
        popup.setX(Double.NaN); // not to update X-cord when the style of ChoosePiece popup's elements changed
        popup.setY(Double.NaN); // not to update Y-cord when the style of ChoosePiece popup's elements changed
    }

}
