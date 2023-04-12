package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePieceWhitePopupController extends ChoosePiecePoputAbstractController implements Initializable {

    @FXML
    private Pane queen_w;
    @FXML
    private Pane bishop_w;
    @FXML
    private Pane knight_w;
    @FXML
    private Pane rook_w;

    public ChoosePieceWhitePopupController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieces.add(new PaneAndName(queen_w, queen_w.getId()));
        pieces.add(new PaneAndName(bishop_w, bishop_w.getId()));
        pieces.add(new PaneAndName(knight_w, knight_w.getId()));
        pieces.add(new PaneAndName(rook_w, rook_w.getId()));
    }

}
