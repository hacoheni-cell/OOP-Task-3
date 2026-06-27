package tests.unitsTest;

import Game.Cell;
import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Player;
import Units.Trap;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrapTest {
    private List<String> sentMessages;
    private CombatSystem testCombatSystem;
    private Trap testTrap;
    private boolean[] attackCalled;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        attackCalled = new boolean[]{false};

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                return false;
            }
        };

        testTrap = new Trap(2, 3, 0, true, 10, "DummyTrap", 50, 50, 20, 5,
                new Position(0, 0), 3, "T", testCombatSystem, msg -> sentMessages.add(msg));
    }

    @AfterEach
    void tearDown() {
        sentMessages.clear();
        testTrap = null;
        testCombatSystem = null;
        attackCalled = null;
    }

    private Player createDummyPlayer(int x, int y, int health, int defence) {
        return new Player("DummyPlayer", health, 10, defence, new Position(x, y), "@",
                testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public int Cast(List<Unit> listOfUnits) { return 0; }
            @Override
            public boolean Cast(Enemy enemy) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
            @Override
            public boolean AttackAccept(Unit other) {
                attackCalled[0] = true;
                return super.AttackAccept(other);
            }
        };
    }

    private GameContext createDummyContext(List<Unit> targets) {
        return new GameContext() {
            @Override
            public Position getPlayerPos() {
                return new Position(0, 0);
            }
            @Override
            public List<Unit> getUnitsInRange(Position p, double range) {
                return targets;
            }
            @Override
            public void clearCell(Position p) {}
            @Override
            public Cell getCell(Position old, int dx, int dy) {
                return null;
            }
        };
    }

    @Test
    void testConstructorAndDescription() {
        String description = testTrap.Description();

        assertTrue(description.contains("Vision Range: 3"));
        assertEquals(3, testTrap.GetRange());
        assertEquals("T", testTrap.toString());
    }

    @Test
    void testGameTick_VisibilityCycle() {
        assertEquals("T", testTrap.toString());

        testTrap.GameTick();
        assertEquals("T", testTrap.toString());

        testTrap.GameTick();
        assertEquals("T", testTrap.toString());

        testTrap.GameTick();
        assertEquals(".", testTrap.toString());

        testTrap.GameTick();
        assertEquals(".", testTrap.toString());

        testTrap.GameTick();
        assertEquals(".", testTrap.toString());

        testTrap.GameTick();
        assertEquals("T", testTrap.toString());
    }

    @Test
    void testCast_VisibleAndInRange_DealsDamage() {
        Player player = createDummyPlayer(0, 1, 100, 5);

        testTrap.cast(player);

        assertEquals(85, player.getHealthAmount());
        assertTrue(sentMessages.contains("DummyTrap hit DummyPlayer for 15 damage."));
    }

    @Test
    void testCast_Invisible_NoDamage() {
        Player player = createDummyPlayer(0, 1, 100, 5);

        testTrap.GameTick();
        testTrap.GameTick();
        testTrap.GameTick();

        assertEquals(".", testTrap.toString());

        testTrap.cast(player);

        assertEquals(100, player.getHealthAmount());
        assertFalse(sentMessages.contains("DummyTrap hit DummyPlayer for 15 damage."));
    }

    @Test
    void testCast_OutOfRange_NoDamage() {
        Player player = createDummyPlayer(5, 5, 100, 5);

        testTrap.cast(player);

        assertEquals(100, player.getHealthAmount());
        assertFalse(sentMessages.contains("DummyTrap hit DummyPlayer for 15 damage."));
    }

    @Test
    void testTakeTurn_AttacksUnitsInRange() {
        Player player = createDummyPlayer(0, 1, 100, 5);
        List<Unit> targets = new ArrayList<>();
        targets.add(player);
        GameContext ctx = createDummyContext(targets);

        testTrap.takeTurn(ctx);

        assertTrue(attackCalled[0]);
        assertEquals(85, player.getHealthAmount());
    }
}