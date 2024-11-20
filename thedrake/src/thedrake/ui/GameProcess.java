package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import thedrake.GameState;
import thedrake.PlayingSide;

public class GameProcess extends BorderPane {
    private BoardView boardView;

    private VBox infoBox;

    private Label playerOnTurn;
    private Label orangeCapturedTroops;
    private Label blueCapturedTroops;

    public GameProcess(GameState gameState) {
        this.boardView = new BoardView(gameState);
        this.setCenter(boardView);

        // Создаем контейнер для стека Orange
        VBox orangeStackBox = new VBox();
        orangeStackBox.getChildren().add(new Label("Orange stack:"));
        orangeStackBox.getChildren().add(boardView.getOrangeStackView());
        this.orangeCapturedTroops = new Label("Captured: 0");
        orangeStackBox.getChildren().add(this.orangeCapturedTroops);
        orangeStackBox.setAlignment(Pos.TOP_CENTER);
        orangeStackBox.setSpacing(5);
        orangeStackBox.setPadding(new Insets(10, 0, 0, 15));  // Отступы слева

        // Создаем контейнер для стека Blue
        VBox blueStackBox = new VBox();
        blueStackBox.getChildren().add(new Label("Blue stack:"));
        blueStackBox.getChildren().add(boardView.getBlueStackView());
        this.blueCapturedTroops = new Label("Captured: 0");
        blueStackBox.getChildren().add(this.blueCapturedTroops);
        blueStackBox.setAlignment(Pos.TOP_CENTER);
        blueStackBox.setSpacing(5);
        blueStackBox.setPadding(new Insets(10, 15, 0, 0));  // Отступы справа

        this.infoBox = new VBox();
        this.playerOnTurn = new Label();

        infoBox.getChildren().add(this.playerOnTurn);

        infoBox.setAlignment(Pos.CENTER);

        this.setTop(infoBox);
        this.setLeft(orangeStackBox);  // Устанавливаем стек Orange слева
        this.setRight(blueStackBox);   // Устанавливаем стек Blue справа

        blueStackBox.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #000000;");
        orangeStackBox.setStyle("-fx-font-weight: bold; -fx-font-size: 19px; -fx-text-fill: #000000;");
        orangeCapturedTroops.setStyle("-fx-font-weight: bold; -fx-font-size: 17px; -fx-text-fill: #e08a5b;");
        blueCapturedTroops.setStyle("-fx-font-weight: bold; -fx-font-size: 17px; -fx-text-fill: #4b90c7;");
    }

    public Label getPlayerOnTurn() {
        if ("♦Side on turn: ORANGE".equals(playerOnTurn.getText())) {
            playerOnTurn.setStyle("-fx-font-weight: bold; -fx-font-size: 30px; -fx-underline: true; -fx-text-fill: #e08a5b;");
        } else {
            playerOnTurn.setStyle("-fx-font-weight: bold; -fx-font-size: 30px; -fx-underline: true; -fx-text-fill: #4b90c7;");
        }

        return playerOnTurn;
    }

    public void updateCapturedTroops(GameState gameState) {
        int orangeCaptured = gameState.army(PlayingSide.ORANGE).captured().size();
        int blueCaptured = gameState.army(PlayingSide.BLUE).captured().size();
        orangeCapturedTroops.setText("Captured: " + orangeCaptured);
        blueCapturedTroops.setText("Captured: " + blueCaptured);
    }

    public BoardView getBoardView() {
        return boardView;
    }
}
