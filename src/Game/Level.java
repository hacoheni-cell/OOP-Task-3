package Game;

import Combat_System.CombatSystem;
import Units.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Level {
    private List<Enemy> enemies;
    private GameBoard gameBoard;
    private Position playerInitalPosition;

    public Level(List<String> levelData, CombatSystem combatSys) {
        parseLevel(levelData,combatSys);
    }

    public GameBoard getBoard() {
        return gameBoard;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean isCleared() {
        return enemies.isEmpty();
    }

    public void removeDeadEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Unit enemy = iterator.next();
            if (enemy.isDead()) {
                Position deadEnemyPos = enemy.getPosition();
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
    private void parseLevel(List<String> levelData, CombatSystem combatSystem) {
        int height = levelData.size();
        int width = levelData.get(0).length();
        this.gameBoard = new GameBoard(width, height);
        this.enemies = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            String row = levelData.get(y);
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                Position pos = new Position(x, y);
                if (c == '#') {
                    gameBoard.setCell(x, y, new Wall(pos));
                }
                else {
                    Floor floor = new Floor(pos, null);
                    gameBoard.setCell(x, y, floor);
                    if (c == '.') {
                        gameBoard.setCell(x, y, floor);
                    }
                    else if (c == '@') {
                        playerInitalPosition = pos;
                    }
                    else {
                        Enemy enemy = TileProvider.createEnemy(c, pos, combatSystem);
                        if (enemy != null) {
                            enemies.add(enemy);
                            floor.setOccupant(enemy);
                        }
                    }
                }
            }
        }
    }

    public Position getPlayerInitalPosition() {
        return this.playerInitalPosition;
    }

    public void setPlayerInInitPos(Player currentPlayer) {
        Floor floor = new Floor(playerInitalPosition,currentPlayer);
        gameBoard.setCell(playerInitalPosition.getX(),playerInitalPosition.getY(),floor);
    }
}