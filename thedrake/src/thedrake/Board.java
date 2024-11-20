package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

    private final int dimension;
    private PositionFactory positionFactory;
    private BoardTile[][] array;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        this.positionFactory = new PositionFactory(dimension);
        this.array = new BoardTile[dimension][dimension];

        for (int i=0; i<dimension; i++){
            for (int j=0; j<dimension; j++){
                this.array[i][j] = BoardTile.EMPTY;
            }
        }
    }

    // Rozměr hrací desky
    public int dimension() {
        return this.dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return array[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board nBoard = new Board(dimension);

        for(int i=0; i<dimension; i++){
            for(int j=0; j<dimension; j++){
                nBoard.array[i][j] = this.array[i][j];
            }
        }

        for(TileAt at : ats){
            nBoard.array[at.pos.i()][at.pos.j()] = at.tile;
        }

        return nBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return positionFactory;
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"dimension\":%d,\"tiles\":[", dimension);
        int counter = 0;
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
                array[j][i].toJSON(writer);
                counter++;
                if (counter < dimension * dimension)
                    writer.print(",");
            }
        }
        writer.print("]}");
    }
}

