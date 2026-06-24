package Game;

import java.util.List;
import Units.Unit;
import Units.Player;

public interface GameContext {
    Cell getCell(Position p, int x, int y);
    List<Unit> getUnitsInRange(Position position, double range);
    Position getPlayerPos();
    void moveUnit(Unit unit, Position newPos);
}