package thedrake.ui;

import thedrake.GameState;
import thedrake.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);
    void stackViewSelected(StackView stackView);
    GameState getGameState();

}
