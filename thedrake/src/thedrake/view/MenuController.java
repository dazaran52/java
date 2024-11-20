package thedrake.view;

import thedrake.GameResult;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;


public class MenuController {

    @FXML private Button versusModeButton;
    @FXML private Button singlePlayerButton;
    @FXML private Button multiPlayerButton;

    @FXML
    private Button closeButton;

    @FXML
    private void closeApplication() {
        Platform.exit();
    }

    public void versusModeAction(ActionEvent actionEvent) {
        GameResult.changeState(GameResult.IN_PLAY);
    }

    public void singlePlayerAction(ActionEvent actionEvent) {
    }

    public void multiPlayerAction(ActionEvent actionEvent) {

    }

}