package thedrake;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;
public class TroopTile implements Tile, JSONSerializable{

    private final Troop troop;
    private final PlayingSide playingSide;
    private final TroopFace troopFace;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.playingSide = side;
        this.troopFace = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side(){
        return playingSide;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face(){
        return troopFace;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop(){
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    @Override
    public boolean canStepOn(){
        return false;
    }

    // Vrací True, protože na dlaždici je jednotka
    @Override
    public boolean hasTroop(){
        return true;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    public TroopTile flipped(){
        if (troopFace == TroopFace.AVERS){
            return new TroopTile(this.troop, this.playingSide, TroopFace.REVERS);
        }
        else {
            return new TroopTile(this.troop, this.playingSide, TroopFace.AVERS);
        }
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state){
        List<Move> result = new ArrayList<>();
        List<TroopAction> actionList = troop.actions(troopFace);
        for (TroopAction z : actionList){
            result.addAll(z.movesFrom(pos, playingSide, state));
        }
        return result;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"troop\":");
        troop.toJSON(writer);
        writer.print(",\"side\":");
        playingSide.toJSON(writer);
        writer.print(",\"face\":");
        troopFace.toJSON(writer);
        writer.print("}");
    }

}
