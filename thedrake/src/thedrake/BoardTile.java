package thedrake;

import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;

public interface BoardTile extends Tile, JSONSerializable {
    BoardTile EMPTY = new BoardTile() {

        @Override
        public boolean canStepOn() {
            return true;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "empty";
        }

        @Override
        public void toJSON(PrintWriter writer){
            writer.print("\"empty\"");
        }
    };

    BoardTile MOUNTAIN = new BoardTile() {
        @Override
        public boolean canStepOn() {
            return false;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }

        @Override
        public List<Move> movesFrom(BoardPos pos, GameState state) {
            return Collections.emptyList();
        }

        @Override
        public String toString() {
            return "mountain";
        }

        @Override
        public void toJSON(PrintWriter writer){
            writer.print("\"mountain\"");
        }
    };
}
