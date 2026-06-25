package Game;

import Game.Position;
import Units.Unit;

public class Wall extends Cell {
    public Wall(Position p) {
        super(p,'#');
    }

    @Override
    public boolean Accept(Unit unit) {
        return false;
    }
}