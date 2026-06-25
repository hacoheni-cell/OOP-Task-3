package Game;

import Units.Enemy;
import Units.Unit;

public class Floor extends Cell {
    private Unit occupant;

    public Floor(Position p, Unit u) {
        super(p, ".");
        this.occupant = u;
    }

    @Override
    public boolean Accept(Unit unit) {
        if (occupant == null) {
            occupant = unit;
            return true;
        } else {
            if (unit.Advance(this.occupant)) {
                occupant = unit;
                return true;
            }
            return false;
        }
    }

    @Override
    public Unit getOccupant() {
        return this.occupant;
    }

    public void setOccupant(Unit unit) {
        occupant = unit;
    }
    @Override
    public String toString() {
        if (this.occupant != null) {
            return occupant.toString();
        }
        return String.valueOf(this.tile);
    }
}