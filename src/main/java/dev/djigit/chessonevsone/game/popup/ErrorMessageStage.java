package dev.djigit.chessonevsone.game.popup;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class ErrorMessageStage extends Stage {
    private String error;

    public ErrorMessageStage(String error) {
        super();

        this.error = error;
        init();
    }

    private void init() {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Error occurred.");

        URL url = getClass().getResource("/scenes/ErrorMessagePopUp.fxml");
        Scene scene = new Scene(FXMLLoaderFactory.getRootNode(url));

        setErrorMessageOnScene(scene);
        setCloseActionOnOkButton(scene);

        setScene(scene);
    }

    private void setErrorMessageOnScene(Scene scene) {
        ((Label) scene.lookup("#error_label")).setText(error);
    }

    private void setCloseActionOnOkButton(Scene scene) {
        ((Button) scene.lookup("#ok_button")).addEventHandler(
                MouseEvent.MOUSE_CLICKED,
                me -> close()
                );
    }
}
