package thedrake.view;

import thedrake.GameResult;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {

    @FXML private Button backToMenuButton;
    @FXML private Label wonLabel;

    public void initialize(URL location, ResourceBundle resources) {}

    public void BackToMenuButton() {
        GameResult.changeState(GameResult.MENU);
    }

    @FXML
    public void setWonText(String text, String color) {
        wonLabel.setText(text);
        wonLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
