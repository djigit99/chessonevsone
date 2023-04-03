package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.popup.ConnectingStage;
import dev.djigit.chessonevsone.game.popup.ErrorMessageStage;
import dev.djigit.chessonevsone.sockets.CantCreateServerException;
import dev.djigit.chessonevsone.sockets.GameCreatorSocket;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminPlayer extends Player {

    public AdminPlayer(Stage primaryStage) {
        super(primaryStage);
    }

    public void init() {
        super.init();

        showChooseColorScene();
    }

    private void showChooseColorScene() {
        final String CHOOSE_COLOR_SCENE_URL = "/scenes/ChooseColor.fxml";

        VBox chooseColorNode = (VBox) FXMLLoaderFactory.getRootNode(getClass().getResource(CHOOSE_COLOR_SCENE_URL));
        Stage chooseColorStage = new Stage();

        chooseColorStage.setScene(new Scene(chooseColorNode));
        chooseColorStage.initModality(Modality.APPLICATION_MODAL);
        chooseColorStage.setResizable(false);
        chooseColorStage.setOnCloseRequest(null);
        chooseColorStage.show();
        chooseColorStage.setX(getPrimaryStage().getX() + getPrimaryStage().getWidth() / 2 - chooseColorStage.getWidth() / 2);
        chooseColorStage.setY(getPrimaryStage().getY() + getPrimaryStage().getHeight() / 2 - chooseColorStage.getHeight() / 2);

        getPrimaryStage().xProperty().addListener(getStageWidthChangedListener(chooseColorStage));
        getPrimaryStage().yProperty().addListener(getStageHeightChangedListener(chooseColorStage));

        addListenersForChooseColorButtons(chooseColorStage);
    }

    private ChangeListener<Number> getStageWidthChangedListener(Stage chooseColorStage) {
        return (observable, oldValue, newValue) ->
                chooseColorStage.setX(getPrimaryStage().getX() + getPrimaryStage().getWidth() / 2 - chooseColorStage.getWidth() / 2);
    }

    private ChangeListener<Number> getStageHeightChangedListener(Stage chooseColorStage) {
        return (observable, oldValue, newValue) ->
                chooseColorStage.setY(getPrimaryStage().getY() + getPrimaryStage().getHeight() / 2 - chooseColorStage.getHeight() / 2);
    }

    private void addListenersForChooseColorButtons(Stage chooseColorStage) {
        Parent chooseColorNode = chooseColorStage.getScene().getRoot();
        Button whiteColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(0);
        Button blackColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(1);

        EventHandler<MouseEvent> setToWhiteHandler = me -> {
            System.out.println("Server: server picked up a white side");
            setColor(Color.WHITE);
            chooseColorStage.close();
            lookForAnOpponent();
        };

        EventHandler<MouseEvent> setToBlackHandler = me -> {
            System.out.println("Server: server picked up a black side");
            setColor(Color.BLACK);
            chooseColorStage.close();
            lookForAnOpponent();
        };

        whiteColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToWhiteHandler);
        blackColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToBlackHandler);
    }

    private void lookForAnOpponent() {
        try {
            socket = GameCreatorSocket.getInstance();

            ConnectingStage connectingStage = new ConnectingStage();
            connectingStage.setOnCloseRequest(we -> socket.close());

            Thread waitForOpponentThread = new Thread(() -> {
                serverSocket().startServer(getColor());
                Platform.runLater(() -> {
                    connectingStage.close();
                    showChessBoard();
                });
            });
            waitForOpponentThread.setDaemon(true);
            waitForOpponentThread.start();

            connectingStage.show();

            connectingStage.setX(getCenterXForChessBoard() + getPrimaryStage().getWidth() / 2 - connectingStage.getWidth() / 2);
            connectingStage.setY(getCenterYForChessBoard() + getPrimaryStage().getHeight() / 2 - connectingStage.getHeight() / 2);
        } catch (CantCreateServerException ex) {
            ErrorMessageStage error = new ErrorMessageStage(ex.getMessage());
            error.show();
            throw new RuntimeException(ex);
        }
    }

    private GameCreatorSocket serverSocket() {
        return (GameCreatorSocket) socket;
    }
}
