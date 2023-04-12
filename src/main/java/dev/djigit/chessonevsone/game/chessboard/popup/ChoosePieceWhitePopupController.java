package dev.djigit.chessonevsone.game.chessboard.popup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePieceWhitePopupController extends ChoosePiecePoputAbstractController implements Initializable {

    @FXML
    private ImageView queen_w;
    @FXML
    private ImageView bishop_w;
    @FXML
    private ImageView knight_w;
    @FXML
    private ImageView rook_w;

    public ChoosePieceWhitePopupController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieces.add(new ImageViewAndName(queen_w, queen_w.getId()));
        pieces.add(new ImageViewAndName(bishop_w, bishop_w.getId()));
        pieces.add(new ImageViewAndName(knight_w, knight_w.getId()));
        pieces.add(new ImageViewAndName(rook_w, rook_w.getId()));
    }

}
