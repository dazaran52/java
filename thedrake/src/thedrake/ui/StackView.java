package thedrake.ui;

import thedrake.Tile;
import thedrake.TroopFace;
import thedrake.TroopTile;
import thedrake.PlayingSide;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StackView extends Pane {

    private Tile tile;

    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final Border selectBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private TileViewContext tileViewContext;
    private PlayingSide side;

    public StackView(TileViewContext context, PlayingSide side) {
        this.setPrefSize(100, 100);

        setOnMouseClicked(e -> onClick());

        this.tileViewContext = context;
        this.side = side;

        update();
    }

    public void update() {
        if (!tileViewContext.getGameState().army(side).stack().isEmpty()){
            setTile(new TroopTile(tileViewContext.getGameState().army(side).stack().get(0), side, TroopFace.AVERS));
        } else {
            setTile(null);
        }

        this.setBackground(backgrounds.get(tile));
    }

    public void select() {
        this.setBorder(selectBorder);
        tileViewContext.stackViewSelected(this);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void unselect() {
        this.setBorder(null);
    }

    public void onClick() {
        select();
    }

    public PlayingSide side(){
        return this.side;
    }
}