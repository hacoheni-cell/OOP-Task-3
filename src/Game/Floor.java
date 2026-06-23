package Game;

import Units.Unit;

public class Floor extends Cell {
    private Unit occupent;

    public Floor(Position p, Unit u) {
        super(p, '.');
        this.occupent = u;
    }

    @Override
    public boolean Accept(Unit unit) {
        if (occupent == null) {
            occupent = unit;
            return true;
        } else {
            if (unit.Advance(this.occupent)) {
                occupent = unit;
                return true;
            }
            return false;
        }
    }

    @Override
    public Unit getOccupant() {
        return this.occupent;
    }
}