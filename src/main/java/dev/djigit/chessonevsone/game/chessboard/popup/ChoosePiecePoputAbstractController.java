package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.List;

public abstract class ChoosePiecePoputAbstractController {
    protected final List<PaneAndName> pieces;

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

    static class PaneAndName {
        private final Pane pane;
        private final String name;

        public PaneAndName(Pane pane, String name) {
            this.pane = pane;
            this.name = name;
        }

        public Pane getPane() {
            return pane;
        }

        public String getName() {
            return name;
        }
    }

    public List<PaneAndName> getPieces() {
        return pieces;
    }
}
