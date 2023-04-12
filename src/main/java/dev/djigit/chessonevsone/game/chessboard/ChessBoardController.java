package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChessBoardController implements Initializable {

    private final ArrayList<CellPaneAndCoords> cells;
    private final ArrayList<ImageViewAndName> imageViewsAndNames;
    @FXML
    private Pane panel_a1;
    @FXML
    private ImageView rook_w_a1;
    @FXML
    private Pane panel_a2;
    @FXML
    private ImageView pawn_w_a2;
    @FXML
    private Pane panel_a3;
    @FXML
    private Pane panel_a4;
    @FXML
    private Pane panel_a5;
    @FXML
    private Pane panel_a6;
    @FXML
    private Pane panel_a7;
    @FXML
    private ImageView pawn_b_a7;
    @FXML
    private Pane panel_a8;
    @FXML
    private ImageView rook_b_a8;

    @FXML
    private Pane panel_b1;
    @FXML
    private ImageView knight_w_b1;
    @FXML
    private Pane panel_b2;
    @FXML
    private ImageView pawn_w_b2;
    @FXML
    private Pane panel_b3;
    @FXML
    private Pane panel_b4;
    @FXML
    private Pane panel_b5;
    @FXML
    private Pane panel_b6;
    @FXML
    private Pane panel_b7;
    @FXML
    private ImageView pawn_b_b7;
    @FXML
    private Pane panel_b8;
    @FXML
    private ImageView knight_b_b8;

    @FXML
    private Pane panel_c1;
    @FXML
    private ImageView bishop_w_c1;
    @FXML
    private Pane panel_c2;
    @FXML
    private ImageView pawn_w_c2;
    @FXML
    private Pane panel_c3;
    @FXML
    private Pane panel_c4;
    @FXML
    private Pane panel_c5;
    @FXML
    private Pane panel_c6;
    @FXML
    private Pane panel_c7;
    @FXML
    private ImageView pawn_b_c7;
    @FXML
    private Pane panel_c8;
    @FXML
    private ImageView bishop_b_c8;

    @FXML
    private Pane panel_d1;
    @FXML
    private ImageView queen_w_d1;
    @FXML
    private Pane panel_d2;
    @FXML
    private ImageView pawn_w_d2;
    @FXML
    private Pane panel_d3;
    @FXML
    private Pane panel_d4;
    @FXML
    private Pane panel_d5;
    @FXML
    private Pane panel_d6;
    @FXML
    private Pane panel_d7;
    @FXML
    private ImageView pawn_b_d7;
    @FXML
    private Pane panel_d8;
    @FXML
    private ImageView queen_b_d8;

    @FXML
    private Pane panel_e1;
    @FXML
    private ImageView king_w_e1;
    @FXML
    private Pane panel_e2;
    @FXML
    private ImageView pawn_w_e2;
    @FXML
    private Pane panel_e3;
    @FXML
    private Pane panel_e4;
    @FXML
    private Pane panel_e5;
    @FXML
    private Pane panel_e6;
    @FXML
    private Pane panel_e7;
    @FXML
    private ImageView pawn_b_e7;
    @FXML
    private Pane panel_e8;
    @FXML
    private ImageView king_b_e8;

    @FXML
    private Pane panel_f1;
    @FXML
    private ImageView bishop_w_f1;
    @FXML
    private Pane panel_f2;
    @FXML
    private ImageView pawn_w_f2;
    @FXML
    private Pane panel_f3;
    @FXML
    private Pane panel_f4;
    @FXML
    private Pane panel_f5;
    @FXML
    private Pane panel_f6;
    @FXML
    private Pane panel_f7;
    @FXML
    private ImageView pawn_b_f7;
    @FXML
    private Pane panel_f8;
    @FXML
    private ImageView bishop_b_f8;

    @FXML
    private Pane panel_g1;
    @FXML
    private ImageView knight_w_g1;
    @FXML
    private Pane panel_g2;
    @FXML
    private ImageView pawn_w_g2;
    @FXML
    private Pane panel_g3;
    @FXML
    private Pane panel_g4;
    @FXML
    private Pane panel_g5;
    @FXML
    private Pane panel_g6;
    @FXML
    private Pane panel_g7;
    @FXML
    private ImageView pawn_b_g7;
    @FXML
    private Pane panel_g8;
    @FXML
    private ImageView knight_b_g8;

    @FXML
    private Pane panel_h1;
    @FXML
    private ImageView rook_w_h1;
    @FXML
    private Pane panel_h2;
    @FXML
    private ImageView pawn_w_h2;
    @FXML
    private Pane panel_h3;
    @FXML
    private Pane panel_h4;
    @FXML
    private Pane panel_h5;
    @FXML
    private Pane panel_h6;
    @FXML
    private Pane panel_h7;
    @FXML
    private ImageView pawn_b_h7;
    @FXML
    private Pane panel_h8;
    @FXML
    private ImageView rook_b_h8;

    public ChessBoardController() {
        cells = new ArrayList<>();
        imageViewsAndNames = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cells.add(new CellPaneAndCoords(panel_a1, CellModel.Coords.A1));
        imageViewsAndNames.add(new ImageViewAndName(rook_w_a1, "rook_w_a1"));
        cells.add(new CellPaneAndCoords(panel_a2, CellModel.Coords.A2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_a2, "pawn_w_a2"));
        cells.add(new CellPaneAndCoords(panel_a3, CellModel.Coords.A3));
        cells.add(new CellPaneAndCoords(panel_a4, CellModel.Coords.A4));
        cells.add(new CellPaneAndCoords(panel_a5, CellModel.Coords.A5));
        cells.add(new CellPaneAndCoords(panel_a6, CellModel.Coords.A6));
        cells.add(new CellPaneAndCoords(panel_a7, CellModel.Coords.A7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_a7, "pawn_b_a7"));
        cells.add(new CellPaneAndCoords(panel_a8, CellModel.Coords.A8));
        imageViewsAndNames.add(new ImageViewAndName(rook_b_a8, "rook_b_a8"));


        cells.add(new CellPaneAndCoords(panel_b1, CellModel.Coords.B1));
        imageViewsAndNames.add(new ImageViewAndName(knight_w_b1, "knight_w_b1"));
        cells.add(new CellPaneAndCoords(panel_b2, CellModel.Coords.B2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_b2, "pawn_w_b2"));
        cells.add(new CellPaneAndCoords(panel_b3, CellModel.Coords.B3));
        cells.add(new CellPaneAndCoords(panel_b4, CellModel.Coords.B4));
        cells.add(new CellPaneAndCoords(panel_b5, CellModel.Coords.B5));
        cells.add(new CellPaneAndCoords(panel_b6, CellModel.Coords.B6));
        cells.add(new CellPaneAndCoords(panel_b7, CellModel.Coords.B7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_b7, "pawn_b_b7"));
        cells.add(new CellPaneAndCoords(panel_b8, CellModel.Coords.B8));
        imageViewsAndNames.add(new ImageViewAndName(knight_b_b8, "knight_b_b8"));

        cells.add(new CellPaneAndCoords(panel_c1, CellModel.Coords.C1));
        imageViewsAndNames.add(new ImageViewAndName(bishop_w_c1, "bishop_w_c1"));
        cells.add(new CellPaneAndCoords(panel_c2, CellModel.Coords.C2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_c2, "pawn_w_c2"));
        cells.add(new CellPaneAndCoords(panel_c3, CellModel.Coords.C3));
        cells.add(new CellPaneAndCoords(panel_c4, CellModel.Coords.C4));
        cells.add(new CellPaneAndCoords(panel_c5, CellModel.Coords.C5));
        cells.add(new CellPaneAndCoords(panel_c6, CellModel.Coords.C6));
        cells.add(new CellPaneAndCoords(panel_c7, CellModel.Coords.C7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_c7, "pawn_b_c7"));
        cells.add(new CellPaneAndCoords(panel_c8, CellModel.Coords.C8));
        imageViewsAndNames.add(new ImageViewAndName(bishop_b_c8, "bishop_b_c8"));

        cells.add(new CellPaneAndCoords(panel_d1, CellModel.Coords.D1));
        imageViewsAndNames.add(new ImageViewAndName(queen_w_d1, "queen_w_d1"));
        cells.add(new CellPaneAndCoords(panel_d2, CellModel.Coords.D2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_d2, "pawn_w_d2"));
        cells.add(new CellPaneAndCoords(panel_d3, CellModel.Coords.D3));
        cells.add(new CellPaneAndCoords(panel_d4, CellModel.Coords.D4));
        cells.add(new CellPaneAndCoords(panel_d5, CellModel.Coords.D5));
        cells.add(new CellPaneAndCoords(panel_d6, CellModel.Coords.D6));
        cells.add(new CellPaneAndCoords(panel_d7, CellModel.Coords.D7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_d7, "pawn_b_d7"));
        cells.add(new CellPaneAndCoords(panel_d8, CellModel.Coords.D8));
        imageViewsAndNames.add(new ImageViewAndName(queen_b_d8, "queen_b_d8"));

        cells.add(new CellPaneAndCoords(panel_e1, CellModel.Coords.E1));
        imageViewsAndNames.add(new ImageViewAndName(king_w_e1, "king_w_e1"));
        cells.add(new CellPaneAndCoords(panel_e2, CellModel.Coords.E2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_e2, "pawn_w_e2"));
        cells.add(new CellPaneAndCoords(panel_e3, CellModel.Coords.E3));
        cells.add(new CellPaneAndCoords(panel_e4, CellModel.Coords.E4));
        cells.add(new CellPaneAndCoords(panel_e5, CellModel.Coords.E5));
        cells.add(new CellPaneAndCoords(panel_e6, CellModel.Coords.E6));
        cells.add(new CellPaneAndCoords(panel_e7, CellModel.Coords.E7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_e7, "pawn_b_e7"));
        cells.add(new CellPaneAndCoords(panel_e8, CellModel.Coords.E8));
        imageViewsAndNames.add(new ImageViewAndName(king_b_e8, "king_b_e8"));

        cells.add(new CellPaneAndCoords(panel_f1, CellModel.Coords.F1));
        imageViewsAndNames.add(new ImageViewAndName(bishop_w_f1, "bishop_w_f1"));
        cells.add(new CellPaneAndCoords(panel_f2, CellModel.Coords.F2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_f2, "pawn_w_f2"));
        cells.add(new CellPaneAndCoords(panel_f3, CellModel.Coords.F3));
        cells.add(new CellPaneAndCoords(panel_f4, CellModel.Coords.F4));
        cells.add(new CellPaneAndCoords(panel_f5, CellModel.Coords.F5));
        cells.add(new CellPaneAndCoords(panel_f6, CellModel.Coords.F6));
        cells.add(new CellPaneAndCoords(panel_f7, CellModel.Coords.F7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_f7, "pawn_b_f7"));
        cells.add(new CellPaneAndCoords(panel_f8, CellModel.Coords.F8));
        imageViewsAndNames.add(new ImageViewAndName(bishop_b_f8, "bishop_b_f8"));

        cells.add(new CellPaneAndCoords(panel_g1, CellModel.Coords.G1));
        imageViewsAndNames.add(new ImageViewAndName(knight_w_g1, "knight_w_g1"));
        cells.add(new CellPaneAndCoords(panel_g2, CellModel.Coords.G2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_g2, "pawn_w_g2"));
        cells.add(new CellPaneAndCoords(panel_g3, CellModel.Coords.G3));
        cells.add(new CellPaneAndCoords(panel_g4, CellModel.Coords.G4));
        cells.add(new CellPaneAndCoords(panel_g5, CellModel.Coords.G5));
        cells.add(new CellPaneAndCoords(panel_g6, CellModel.Coords.G6));
        cells.add(new CellPaneAndCoords(panel_g7, CellModel.Coords.G7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_g7, "pawn_b_g7"));
        cells.add(new CellPaneAndCoords(panel_g8, CellModel.Coords.G8));
        imageViewsAndNames.add(new ImageViewAndName(knight_b_g8, "knight_b_g8"));

        cells.add(new CellPaneAndCoords(panel_h1, CellModel.Coords.H1));
        imageViewsAndNames.add(new ImageViewAndName(rook_w_h1, "rook_w_h1"));
        cells.add(new CellPaneAndCoords(panel_h2, CellModel.Coords.H2));
        imageViewsAndNames.add(new ImageViewAndName(pawn_w_h2, "pawn_w_h2"));
        cells.add(new CellPaneAndCoords(panel_h3, CellModel.Coords.H3));
        cells.add(new CellPaneAndCoords(panel_h4, CellModel.Coords.H4));
        cells.add(new CellPaneAndCoords(panel_h5, CellModel.Coords.H5));
        cells.add(new CellPaneAndCoords(panel_h6, CellModel.Coords.H6));
        cells.add(new CellPaneAndCoords(panel_h7, CellModel.Coords.H7));
        imageViewsAndNames.add(new ImageViewAndName(pawn_b_h7, "pawn_b_h7"));
        cells.add(new CellPaneAndCoords(panel_h8, CellModel.Coords.H8));
        imageViewsAndNames.add(new ImageViewAndName(rook_b_h8, "rook_b_h8"));
    }

    public ArrayList<CellPaneAndCoords> getCellPanesAndCoords() {
        return cells;
    }

    public ArrayList<ImageViewAndName> getImageViewsAndNames() {
        return imageViewsAndNames;
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

        public CellModel.Coords getPieceCoords() {
            short y = (short) (name.charAt(name.length() - 1) - '0');
            char x = name.charAt(name.length() - 2);

            return CellModel.Coords.getCordsByValue(x, y);
        }
    }

    static class CellPaneAndCoords {
        private final Pane cellPane;
        private final CellModel.Coords coords;

        public CellPaneAndCoords(Pane cellPane, CellModel.Coords coords) {
            this.cellPane = cellPane;
            this.coords = coords;
        }

        public Pane getCellPane() {
            return cellPane;
        }

        public CellModel.Coords getCoords() {
            return coords;
        }
    }
}
