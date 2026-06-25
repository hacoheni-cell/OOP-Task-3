package Game;

import Units.Unit;

public abstract class Cell {
    protected Position pos;
    protected String tile;

    public Cell(Position pos, String tile) {
        this.pos = pos;
        this.tile = tile;
    }

    public String getTile() {
        return tile;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public abstract boolean Accept(Unit unit);

    public Unit getOccupant() {
        return null;
    }
    public String toString() {
        return tile;
    }
}