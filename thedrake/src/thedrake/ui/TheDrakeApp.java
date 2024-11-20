package thedrake.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import thedrake.*;
import thedrake.view.EndGameController;
import javafx.scene.image.Image;
import thedrake.GameResult;
import thedrake.PlayingSide;


public class TheDrakeApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameState gameState = createSampleGameState();

        AnchorPane menuPane = FXMLLoader.load(getClass().getResource("/thedrake/view/menu-view.fxml"));
        GameProcess gamePane = new GameProcess(gameState);
        FXMLLoader loader = new FXMLLoader();
        AnchorPane endView = loader.load(getClass().getResource("/thedrake/view/EndGame.fxml").openStream());

        Scene mainMenuScene = new Scene(menuPane);
        Scene gameScene = new Scene(gamePane);
        Scene endScene = new Scene(endView);


        primaryStage.getIcons().add(new Image(TheDrakeApp.class.getResourceAsStream("/thedrake/view/icon.jpg")));
        primaryStage.setResizable(false);


        mainMenuScene.getStylesheets().add(getClass().getResource("/thedrake/view/style.css").toExternalForm());

        EndGameController controller = loader.getController();

        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("The Drake");
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (GameResult.getStateChanged()) {

                    switch (GameResult.getState()){
                        case IN_PLAY:
                            primaryStage.setScene(gameScene);
                            break;
                        case VICTORY:
                            PlayingSide sideNotOnTurn = gamePane.getBoardView().getGameState().armyNotOnTurn().side();
                            if (sideNotOnTurn == PlayingSide.ORANGE) {
                                controller.setWonText(sideNotOnTurn + " player has won!", "#e08a5b");
                            } else {
                                controller.setWonText(sideNotOnTurn + " player has won!", "#4b90c7");
                            }
                            primaryStage.setScene(endScene);
                            primaryStage.setResizable(false);
                            break;
                        case DRAW:
                            controller.setWonText("Draw!", "F63E85FF");
                            primaryStage.setScene(endScene);
                            primaryStage.setResizable(false);
                            break;
                        case MENU:
                            primaryStage.setScene(mainMenuScene);
                            primaryStage.setResizable(false);
                            break;
                    }

                    primaryStage.show();
                    GameResult.changeStateChanged(false);
                }

                PlayingSide sideOnTurn = gamePane.getBoardView().getGameState().sideOnTurn();

                gamePane.getPlayerOnTurn().setText("♦Side on turn: " + sideOnTurn);
                gamePane.updateCapturedTroops(gamePane.getBoardView().getGameState());            }
        }.start();
    }

    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN),
                new Board.TileAt(positionFactory.pos(3, 2), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

}
