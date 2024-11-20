package thedrake;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable{
    VICTORY, DRAW, IN_PLAY, MENU;

    public static boolean stateChanged = false;
    public static GameResult state = null;

    public static void changeState(GameResult newState) {
        if (state == newState) {
            return;
        }
        state = newState;
        changeStateChanged(true);
    }

    public static void changeStateChanged(boolean newStatus) {
        stateChanged = newStatus;
    }

    public static boolean getStateChanged() {
        return stateChanged;
    }

    public static GameResult getState() {
        return state;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", this.toString());
    }
}