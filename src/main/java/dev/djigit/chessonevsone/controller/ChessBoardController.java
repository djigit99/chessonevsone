package dev.djigit.chessonevsone.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChessBoardController implements Initializable {

    private ArrayList<Pane> cells;
    @FXML
    private Pane panel_a1;
    @FXML
    private Pane panel_a2;
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
    private Pane panel_a8;

    @FXML
    private Pane panel_b1;
    @FXML
    private Pane panel_b2;
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
    private Pane panel_b8;

    @FXML
    private Pane panel_c1;
    @FXML
    private Pane panel_c2;
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
    private Pane panel_c8;

    @FXML
    private Pane panel_d1;
    @FXML
    private Pane panel_d2;
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
    private Pane panel_d8;

    @FXML
    private Pane panel_e1;
    @FXML
    private Pane panel_e2;
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
    private Pane panel_e8;

    @FXML
    private Pane panel_f1;
    @FXML
    private Pane panel_f2;
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
    private Pane panel_f8;

    @FXML
    private Pane panel_g1;
    @FXML
    private Pane panel_g2;
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
    private Pane panel_g8;

    @FXML
    private Pane panel_h1;
    @FXML
    private Pane panel_h2;
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
    private Pane panel_h8;

    public ChessBoardController() {
        cells = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cells.add(panel_a1);
        cells.add(panel_a2);
        cells.add(panel_a3);
        cells.add(panel_a4);
        cells.add(panel_a5);
        cells.add(panel_a6);
        cells.add(panel_a7);
        cells.add(panel_a8);

        cells.add(panel_b1);
        cells.add(panel_b2);
        cells.add(panel_b3);
        cells.add(panel_b4);
        cells.add(panel_b5);
        cells.add(panel_b6);
        cells.add(panel_b7);
        cells.add(panel_b8);

        cells.add(panel_c1);
        cells.add(panel_c2);
        cells.add(panel_c3);
        cells.add(panel_c4);
        cells.add(panel_c5);
        cells.add(panel_c6);
        cells.add(panel_c7);
        cells.add(panel_c8);

        cells.add(panel_d1);
        cells.add(panel_d2);
        cells.add(panel_d3);
        cells.add(panel_d4);
        cells.add(panel_d5);
        cells.add(panel_d6);
        cells.add(panel_d7);
        cells.add(panel_d8);

        cells.add(panel_e1);
        cells.add(panel_e2);
        cells.add(panel_e3);
        cells.add(panel_e4);
        cells.add(panel_e5);
        cells.add(panel_e6);
        cells.add(panel_e7);
        cells.add(panel_e8);

        cells.add(panel_f1);
        cells.add(panel_f2);
        cells.add(panel_f3);
        cells.add(panel_f4);
        cells.add(panel_f5);
        cells.add(panel_f6);
        cells.add(panel_f7);
        cells.add(panel_f8);

        cells.add(panel_g1);
        cells.add(panel_g2);
        cells.add(panel_g3);
        cells.add(panel_g4);
        cells.add(panel_g5);
        cells.add(panel_g6);
        cells.add(panel_g7);
        cells.add(panel_g8);

        cells.add(panel_h1);
        cells.add(panel_h2);
        cells.add(panel_h3);
        cells.add(panel_h4);
        cells.add(panel_h5);
        cells.add(panel_h6);
        cells.add(panel_h7);
        cells.add(panel_h8);
    }

    public ArrayList<Pane> getCellPanes() {
        return cells;
    }



}
