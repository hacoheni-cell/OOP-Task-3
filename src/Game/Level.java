package Game;

import Units.Unit;
import java.util.List;
import java.util.Iterator;

public class Level {
    private List<Unit> enemies;
    private GameBoard gameBoard;

    public Level(GameBoard gameBoard, List<Unit> enemies) {
        this.gameBoard = gameBoard;
        this.enemies = enemies;
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    public List<Unit> getEnemies() {
        return enemies;
    }

    public boolean isCleared() {
        return enemies.isEmpty();
    }

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
    public Cell getCell(Position p, int x, int y){
        return gameBoard.getCell(p.getX()+x, p.getY()+y);
    }
    public List<Unit> getUnitsInRange(Position position, double range){
        return gameBoard.getUnitsInRange(position,range);
    }
}