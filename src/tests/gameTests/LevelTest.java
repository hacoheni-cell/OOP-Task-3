package tests.gameTests;

import Game.Floor;
import Game.Level;
import Game.Position;
import Game.Wall;
import Units.Enemy;
import Units.Unit;
import Units.Warrior;
import combat.CombatSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    private CombatSystem dummyCombatSystem;
    private List<String> sentMessages;
    private Level level;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        dummyCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                return true;
            }
        };

        List<String> levelData = new ArrayList<>();
        levelData.add("###");
        levelData.add("#@#");
        levelData.add("###");
        level = new Level(levelData, dummyCombatSystem, msg -> sentMessages.add(msg));
    }

    @AfterEach
    void tearDown() {
        dummyCombatSystem = null;
        sentMessages.clear();
        level = null;
    }

    private Enemy createDummyEnemy(Position pos, boolean isDead) {
        return new Enemy(10, "DummyEnemy", 50, 50, 10, 5, pos, "s",
                dummyCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(Game.GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
            @Override
            public boolean isDead() { return isDead; }
        };
    }

    @Test
    void testConstructorAndParsing() {
        assertEquals(3, level.getBoard().getWidth());
        assertEquals(3, level.getBoard().getHeight());

        Position expectedInitPos = new Position(1, 1);
        assertEquals(expectedInitPos, level.getPlayerInitalPosition());

        assertTrue(level.getBoard().getCell(0, 0) instanceof Wall);
        assertTrue(level.getBoard().getCell(1, 1) instanceof Floor);
    }

    @Test
    void testSetPlayerAndGetters() {
        Position initPos = level.getPlayerInitalPosition();
        Warrior player = new Warrior("TestPlayer", 100, 10, 10, initPos, dummyCombatSystem, "@", 3, msg -> {});

        level.setPlayer(player);
        level.setPlayerInInitPos(player);

        assertEquals(initPos, level.getPlayerPos());
        Floor initFloor = (Floor) level.getBoard().getCell(initPos.getX(), initPos.getY());
        assertEquals(player, initFloor.getOccupant());
    }

    @Test
    void testIsCleared() {
        assertTrue(level.isCleared());

        Enemy dummyEnemy = createDummyEnemy(new Position(1, 1), false);
        level.getEnemies().add(dummyEnemy);

        assertFalse(level.isCleared());
    }

    @Test
    void testRemoveDeadEnemies() {
        Position enemyPos = new Position(1, 1);
        Enemy deadEnemy = createDummyEnemy(enemyPos, true);

        Floor floor = (Floor) level.getBoard().getCell(1, 1);
        floor.setOccupant(deadEnemy);
        level.getEnemies().add(deadEnemy);

        assertFalse(level.isCleared());

        level.removeDeadEnemies();

        assertTrue(level.isCleared());
        assertNull(floor.getOccupant());
        assertTrue(sentMessages.contains("DummyEnemy died."));
    }

    @Test
    void testGetCell_RelativePosition() {
        Position basePos = new Position(1, 1);

        assertTrue(level.getCell(basePos, -1, -1) instanceof Wall);
        assertTrue(level.getCell(basePos, 0, 0) instanceof Floor);
        assertTrue(level.getCell(basePos, 1, 0) instanceof Wall);
    }
}