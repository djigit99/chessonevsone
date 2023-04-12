package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePieceBlackPopupController extends ChoosePiecePoputAbstractController implements Initializable {
    @FXML
    private Pane queen_b;
    @FXML
    private Pane bishop_b;
    @FXML
    private Pane knight_b;
    @FXML
    private Pane rook_b;

    public ChoosePieceBlackPopupController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieces.add(new PaneAndName(queen_b, queen_b.getId()));
        pieces.add(new PaneAndName(bishop_b, bishop_b.getId()));
        pieces.add(new PaneAndName(knight_b, knight_b.getId()));
        pieces.add(new PaneAndName(rook_b, rook_b.getId()));
    }
}
