package thedrake.ui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import thedrake.*;
import thedrake.GameResult;
import thedrake.PlayingSide;
import thedrake.Move;

import java.util.List;
public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selectedTileView;

    private StackView selectedBlueStackView;
    private StackView selectedOrangeStackView;

    public BoardView(GameState gameState) {
        this.gameState = gameState;


        PositionFactory positionFactory = gameState.board().positionFactory();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int i = x;
                int j = 3 - y;
                BoardPos boardPos = positionFactory.pos(i, j);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        this.validMoves = new ValidMoves(gameState);

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);

        this.selectedBlueStackView = new StackView(this, PlayingSide.BLUE);
        this.selectedBlueStackView.setMaxSize(100, 100);
        this.selectedOrangeStackView = new StackView(this, PlayingSide.ORANGE);
        this.selectedOrangeStackView.setMaxSize(100, 100);
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selectedTileView != null && selectedTileView != tileView) {
            selectedTileView.unselect();
            selectedTileView = null;
            selectedBlueStackView.unselect();
            selectedOrangeStackView.unselect();
        }

        selectedTileView = tileView;
        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void stackViewSelected(StackView stackView) {
        if (selectedTileView != null) {
            selectedTileView.unselect();
        }

        if (stackView.side() == PlayingSide.BLUE){
            selectedBlueStackView = stackView;
            selectedOrangeStackView.unselect();
        } else {
            selectedOrangeStackView = stackView;
            selectedBlueStackView.unselect();
        }
        clearMoves();

        List<Move> validStackMoves = validMoves.movesFromStack();
        showMoves(validStackMoves);
    }

    @Override
    public void executeMove(Move move) {
        if (selectedTileView != null) {
            selectedTileView.unselect();
            selectedTileView = null;
        }

        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        updateTiles();

        GameResult.changeState(gameState.result());
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
            selectedBlueStackView.update();
            selectedOrangeStackView.update();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public StackView getBlueStackView() {
        return selectedBlueStackView;
    }

    public StackView getOrangeStackView() {
        return selectedOrangeStackView;
    }
}