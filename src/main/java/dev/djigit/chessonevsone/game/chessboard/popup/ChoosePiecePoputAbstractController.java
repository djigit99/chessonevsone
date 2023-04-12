package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.List;

public abstract class ChoosePiecePoputAbstractController {
    protected final List<ImageViewAndName> pieces;


    public ChoosePiecePoputAbstractController() {
        pieces = new LinkedList<>();
    }

    @FXML
    private void onHover(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        pane.setStyle("-fx-background-color:#00ff00");
    }

    @FXML
    private void onHoverOut(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        pane.setStyle("-fx-background-color: #ccffcc; -fx-opacity: 0.8");
    }

    static class ImageViewAndName {
        private final ImageView imageView;
        private final String name;

        public ImageViewAndName(ImageView imageView, String name) {
            this.imageView = imageView;
            this.name = name;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public String getName() {
            return name;
        }
    }

    public List<ImageViewAndName> getPieces() {
        return pieces;
    }
}
