package thedrake;

import java.util.TreeSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.text.MessageFormat;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if(this.troopMap.containsKey(pos)){
            return Optional.ofNullable(this.troopMap.get(pos));
        }
        else
            return Optional.empty();
    }

    public PlayingSide playingSide() {
        return this.playingSide;
    }

    public TilePos leaderPosition() {
        if(this.leaderPosition == TilePos.OFF_BOARD){
            return TilePos.OFF_BOARD;
        }
        else
            return this.leaderPosition;
    }

    public int guards() {
        return this.guards;
    }

    public boolean isLeaderPlaced() {
        if(this.leaderPosition == TilePos.OFF_BOARD){
            return false;
        }
        else
            return true;
    }

    public boolean isPlacingGuards() {
        if(this.isLeaderPlaced() && this.guards < 2){
            return true;
        }
        else
            return false;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> posArray = new HashSet<>();
        this.troopMap.forEach((key,value)->{
            if(value.hasTroop()){
                posArray.add(key);
            }
        });
        return posArray;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if(at(target).isPresent()) {
            throw new IllegalArgumentException("Position already occupied.");
        }
        TroopTile nTroopTile = new TroopTile(troop, this.playingSide(), TroopFace.AVERS);
        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, nTroopTile);

        if(!this.isLeaderPlaced()){
            return new BoardTroops(this.playingSide(), newTroopMap, target, this.guards());
        } else if (this.isPlacingGuards()) {
            return new BoardTroops(this.playingSide(), newTroopMap, this.leaderPosition(), guards + 1);
        }
        else {
            return new BoardTroops(this.playingSide(), newTroopMap, this.leaderPosition(), this.guards());
        }
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if(!this.isLeaderPlaced()){
            throw new IllegalStateException("Cannot move troop before the leader is placed.");
        }
        if(this.isPlacingGuards()){
            throw new IllegalStateException("Cannot move troop before guards are placed.");
        }
        if(!at(origin).isPresent() || at(target).isPresent()){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(target, tile.flipped());
        TilePos newLeaderPos;

        if(this.leaderPosition.equals(origin)){
            newLeaderPos = target;
        } else {
            newLeaderPos = this.leaderPosition;
        }

        return new BoardTroops(playingSide(), newTroops, newLeaderPos, guards);

    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if(!this.isLeaderPlaced()){
            throw new IllegalStateException("Cannot move troop before the leader is placed.");
        }
        if(this.isPlacingGuards()){
            throw new IllegalStateException("Cannot move troop before guards are placed.");
        }
        if(!at(target).isPresent()){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);
        TilePos newLeaderPos;

        if(this.leaderPosition.equals(target)){
            newLeaderPos = TilePos.OFF_BOARD;
        } else {
            newLeaderPos = this.leaderPosition;
        }

        return new BoardTroops(this.playingSide, newTroops, newLeaderPos, this.guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"side\":\"" + this.playingSide + "\",\"leaderPosition\":\"" + this.leaderPosition() + "\",\"guards\":" + this.guards() +
                ",\"troopMap\":{" + this.jsonTroopMap() + "}}");
    }

    private String jsonTroopMap() {
        StringBuilder y = new StringBuilder();
        boolean x = true;

        SortedMap<BoardPos, TroopTile> tmpMap = new TreeMap<BoardPos, TroopTile>(new Comparator<BoardPos>() {
            public int compare(BoardPos x1, BoardPos x2) {
                return x1.toString().compareTo(x2.toString());
            }
        });

        for(BoardPos boardPos : this.troopPositions()) {
            tmpMap.put(boardPos, troopMap.get(boardPos));
        }

        for (BoardPos boardPos : tmpMap.keySet()) {
            if (!x) {
                y.append(",");
            }
            y.append(MessageFormat.format("\"{2}\":{0}\"troop\":\"{3}\",\"side\":\"{4}\",\"face\":\"{5}\"{1}",
                    '{', '}', boardPos.toString(), this.at(boardPos).get().troop().name(),
                    this.at(boardPos).get().side(), this.at(boardPos).get().face()));
            x = false;
        }


        return y.toString();
    }

}
