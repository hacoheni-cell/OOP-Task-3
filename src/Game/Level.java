package Game;

import Units.Unit;
import Units.Player;
import java.util.List;
import java.util.Iterator;

public class Level implements GameContext { // <-- מימוש הממשק
    private List<Unit> enemies;
    private GameBoard gameBoard;
    private Player player; // <-- הוספנו את השחקן לקונטקסט של השלב

    public Level(GameBoard gameBoard, List<Unit> enemies, Player player) {
        this.gameBoard = gameBoard;
        this.enemies = enemies;
        this.player = player;
    }


    @Override
    public Cell getCell(Position p, int x, int y) {
        return gameBoard.getCell(p.getX() + x, p.getY() + y);
    }

    @Override
    public List<Unit> getUnitsInRange(Position position, double range) {
        return gameBoard.getUnitsInRange(position, range);
    }

    @Override
    public Position getPlayerPos() {
        return this.player.getPos();
    }

    @Override
    public void moveUnit(Unit unit, Position newPos) {
        Position oldPos = unit.getPos();
        gameBoard.setCell(oldPos.getX(), oldPos.getY(), new Floor(oldPos, null));
        gameBoard.setCell(newPos.getX(), newPos.getY(), new Floor(newPos, unit));
        unit.setPos(newPos);
    }


    public GameBoard getBoard() { return gameBoard; }
    public List<Unit> getEnemies() { return enemies; }
    public boolean isCleared() { return enemies.isEmpty(); }

    public void removeDeadEnemies() {
        Iterator<Unit> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Unit enemy = iterator.next();
            if (enemy.isDead()) {
                Position deadEnemyPos = enemy.getPos();
                Floor emptyFloor = new Floor(deadEnemyPos, null);
                gameBoard.setCell(deadEnemyPos.getX(), deadEnemyPos.getY(), emptyFloor);
                iterator.remove();
            }
        }
    }
}