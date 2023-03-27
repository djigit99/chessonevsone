package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Game {

    private final String INTRO_SCENE_URL = "/scenes/IntroScene.fxml";
    private String playerRole;
    private Stage primaryStage;

    public Game(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {
        showIntroScene();
    }

    private void showIntroScene() {
        Parent introRootNode = FXMLLoaderFactory.getRootNode(getClass().getResource(INTRO_SCENE_URL));
        primaryStage.setScene(new Scene(introRootNode));
        primaryStage.show();

        addListenersForIntroButtons(introRootNode);
    }

    private void addListenersForIntroButtons(Parent introRootNode) {
        Button newGameBtn = (Button) ((VBox) introRootNode).getChildren().get(0);
        Button loadExistingGameBtn = (Button) ((VBox) introRootNode).getChildren().get(1);

        newGameBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvt ->
        {
            setPlayerRole("ADMIN");
            new AdminPlayer(primaryStage).init();
        });
        loadExistingGameBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvt ->
        {
            setPlayerRole("CLIENT");
            new ClientPlayer(primaryStage).init();
        });
    }

    public void close() {
        throw new NotImplementedException();
    }

    private void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }
}
