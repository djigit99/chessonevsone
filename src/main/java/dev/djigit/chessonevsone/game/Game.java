package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.Board;
import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.sockets.GameClientSocket;
import dev.djigit.chessonevsone.sockets.GameCreatorSocket;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Game {

    private final String INTRO_SCENE_URL = "/scenes/IntroScene.fxml";
    private final String CHOOSE_COLOR_SCENE_URL = "/scenes/ChooseColor.fxml";
    private final String CHESSBOARD_FOR_WHITE_SCENE_URL = "/scenes/ChessBoardSceneWhite.fxml";
    private final String CHESSBOARD_FOR_BLACK_SCENE_URL = "/scenes/ChessBoardSceneBlack.fxml";

    private String playerRole;
    private String adminColor;

    private Stage primaryStage;

    public Game(Stage primaryStage, Board board) {
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
            System.out.println("Server: creating new game...");
            setPlayerRole("ADMIN");
            showChooseColorScene();
        });
        loadExistingGameBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvt ->
        {
            setPlayerRole("CLIENT");
            showChessBoard();
        });
    }

    private void showChooseColorScene() {
        Parent chooseColorNode = FXMLLoaderFactory.getRootNode(getClass().getResource(CHOOSE_COLOR_SCENE_URL));
        primaryStage.setScene(new Scene(chooseColorNode));
        primaryStage.show();

        addListenersForChooseColorButtons(chooseColorNode);
    }

    private void addListenersForChooseColorButtons(Parent chooseColorNode) {
        Button whiteColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(0);
        Button blackColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(1);

        whiteColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvt -> {
            System.out.println("Server: server picked up a white side");
            setAdminColor("WHITE");
            showChessBoard();
        });
        blackColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvt -> {
            System.out.println("Server: server picked up a black side");
            setAdminColor("BLACK");
            showChessBoard();
        });
    }

    private void showChessBoard() {
        // waiting for color
        Player.Color color = getPlayerColor();

        Parent chessBoardRootNode;
        URL url;
        if (color == Player.Color.WHITE) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }
        chessBoardRootNode = FXMLLoaderFactory.getRootNode(url);
        primaryStage.setScene(new Scene(chessBoardRootNode));
        primaryStage.show();
    }

    private Player.Color getPlayerColor() {
        if (playerRole.equals("ADMIN")) {
            Player.Color adminPlayerColor = adminColor.equals("WHITE") ? Player.Color.WHITE : Player.Color.BLACK;
            GameCreatorSocket creatorSocket = new GameCreatorSocket();
            creatorSocket.startServer(adminPlayerColor);
            return adminPlayerColor;
        } else {
            GameClientSocket clientSocket = new GameClientSocket();
            clientSocket.connect();
            return clientSocket.getColor();
        }
    }

    public void close() {

    }

    private void setPlayerRole(String playerRole) {
        this.playerRole = playerRole;
    }

    public void setAdminColor(String adminColor) {
        this.adminColor = adminColor;
    }
}
