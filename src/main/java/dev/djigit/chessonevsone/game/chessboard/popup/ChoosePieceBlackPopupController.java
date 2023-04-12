package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePieceBlackPopupController extends ChoosePiecePoputAbstractController implements Initializable {
    @FXML
    private ImageView queen_b;
    @FXML
    private ImageView bishop_b;
    @FXML
    private ImageView knight_b;
    @FXML
    private ImageView rook_b;

    public ChoosePieceBlackPopupController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieces.add(new ImageViewAndName(queen_b, queen_b.getId()));
        pieces.add(new ImageViewAndName(bishop_b, bishop_b.getId()));
        pieces.add(new ImageViewAndName(knight_b, knight_b.getId()));
        pieces.add(new ImageViewAndName(rook_b, rook_b.getId()));
    }
}
