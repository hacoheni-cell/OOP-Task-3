package tests.unitsTest;

import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {
    private Unit testUnit;

    @BeforeEach
    void setUp() {
        testUnit = new Unit("DummyTarget", 100, 100, 20, 10, new Position(0, 0), "D", null, msg -> {}) {
            @Override
            public boolean AttackAccept(Unit unit) { return false; }
            @Override
            public boolean AdvanceAccept(Unit unit) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };
    }

    @Test
    void testSetHealthAmount_NormalAndExceedingPool() {
        testUnit.SetHealthAmount(50);
        assertEquals(50, testUnit.getHealthAmount(), "Health should update to 50.");

        testUnit.SetHealthAmount(150);
        assertEquals(100, testUnit.getHealthAmount(), "Health should be capped at the health pool (100).");
    }

    @Test
    void testSetHealthPool_NegativeAndCapping() {
        testUnit.SetHealthPool(50);
        testUnit.SetHealthAmount(100);
        assertEquals(50, testUnit.getHealthAmount(), "Health should be capped at the NEW health pool (50).");

        testUnit.SetHealthPool(-10);
        testUnit.SetHealthAmount(10);
        assertEquals(0, testUnit.getHealthAmount(), "Health pool should be 0 when set to a negative number.");
    }

    @Test
    void testSetAttackPoints_NormalAndNegative() {
        testUnit.SetAttackPoints(30);
        assertEquals(30, testUnit.getAttackPoints(), "Attack points should update to 30.");

        testUnit.SetAttackPoints(-5);
        assertEquals(0, testUnit.getAttackPoints(), "Attack points should not be negative; should cap at 0.");
    }

    @Test
    void testSetDefencePoints_NormalAndNegative() {
        testUnit.SetDefencePoints(15);
        assertEquals(15, testUnit.getDefencePoints(), "Defense points should update to 15.");

        testUnit.SetDefencePoints(-20);
        assertEquals(0, testUnit.getDefencePoints(), "Defense points should not be negative; should cap at 0.");
    }

    @Test
    void testIsDead_VariousHealthStates() {
        testUnit.SetHealthAmount(10);
        assertFalse(testUnit.isDead(), "Unit should NOT be dead when health > 0.");

        testUnit.SetHealthAmount(0);
        assertTrue(testUnit.isDead(), "Unit should be dead when health is exactly 0.");

        testUnit.SetHealthAmount(-5);
        assertTrue(testUnit.isDead(), "Unit should be dead when health is negative.");
    }

    @Test
    void testGetName_ReturnsConstructorValue() {
        assertEquals("DummyTarget", testUnit.getName(), "getName should return the name given to the constructor.");
    }

    @Test
    void testSetName_UpdatesName() {
        testUnit.setName("NewName");
        assertEquals("NewName", testUnit.getName(), "setName should update the name field.");
    }

    @Test
    void testGetAttackPoints_ReturnsConstructorValue() {
        assertEquals(20, testUnit.getAttackPoints(), "getAttackPoints should return the value given to the constructor.");
    }

    @Test
    void testGetDefencePoints_ReturnsConstructorValue() {
        assertEquals(10, testUnit.getDefencePoints(), "getDefencePoints should return the value given to the constructor.");
    }

    @Test
    void testGetHealthAmount_ReturnsConstructorValue() {
        assertEquals(100, testUnit.getHealthAmount(), "getHealthAmount should return the value given to the constructor.");
    }

    @Test
    void testGetPos_ReturnsConstructorValue() {
        assertEquals(new Position(0, 0), testUnit.getPos(), "getPos should return the position given to the constructor.");
    }

    @Test
    void testSetPosition_UpdatesPosition() {
        testUnit.setPosition(new Position(3, 4));
        assertEquals(new Position(3, 4), testUnit.getPos(), "setPosition should update the position field.");
    }

    @Test
    void testToString_ReturnsTileString() {
        assertEquals("D", testUnit.toString(), "toString should return the tileString given to the constructor.");
    }

    @Test
    void testDescription_ContainsAllCoreFields() {
        String description = testUnit.Description();
        assertTrue(description.contains("DummyTarget"), "Description should contain the unit's name.");
        assertTrue(description.contains("Health: 100/100"), "Description should contain current/max health.");
        assertTrue(description.contains("Attack: 20"), "Description should contain attack points.");
        assertTrue(description.contains("Defence: 10"), "Description should contain defence points.");
    }

    @Test
    void testAdvance_DelegatesToAdvanceAcceptOnGivenUnit() {
        final boolean[] called = {false};
        Unit other = new Unit("Other", 100, 100, 10, 5, new Position(1, 0), "O", null, msg -> {}) {
            @Override
            public boolean AttackAccept(Unit unit) { return false; }
            @Override
            public boolean AdvanceAccept(Unit unit) {
                called[0] = true;
                return true;
            }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        boolean result = testUnit.Advance(other);

        assertTrue(called[0], "Advance should delegate to other.AdvanceAccept(this).");
        assertTrue(result, "Advance should return whatever AdvanceAccept returns.");
    }

    @Test
    void testAttack_DelegatesToAttackAcceptOnGivenUnit() {
        final boolean[] called = {false};
        Unit other = new Unit("Other", 100, 100, 10, 5, new Position(1, 0), "O", null, msg -> {}) {
            @Override
            public boolean AttackAccept(Unit unit) {
                called[0] = true;
                return true;
            }
            @Override
            public boolean AdvanceAccept(Unit unit) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        boolean result = testUnit.Attack(other);

        assertTrue(called[0], "Attack should delegate to other.AttackAccept(this).");
        assertTrue(result, "Attack should return whatever AttackAccept returns.");
    }

    @Test
    void testDefaultAdvanceVisitPlayer_ReturnsFalse() {
        Player player = new Player("DummyPlayer", 100, 20, 5, new Position(1, 0), "P", null, msg -> {}) {
            @Override
            public int Cast(List<Unit> listOfUnits) { return 0; }
            @Override
            public boolean Cast(Enemy enemy) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        assertFalse(testUnit.AdvanceVisit(player), "Unit's default AdvanceVisit(Player) should return false.");
    }

    @Test
    void testDefaultAdvanceVisitEnemy_ReturnsFalse() {
        Enemy enemy = new Enemy(10, "DummyEnemy", 100, 100, 10, 5, new Position(1, 0), "E", null, msg -> {}) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        assertFalse(testUnit.AdvanceVisit(enemy), "Unit's default AdvanceVisit(Enemy) should return false.");
    }

    @Test
    void testDefaultAttackVisitPlayer_ReturnsFalse() {
        Player player = new Player("DummyPlayer", 100, 20, 5, new Position(1, 0), "P", null, msg -> {}) {
            @Override
            public int Cast(List<Unit> listOfUnits) { return 0; }
            @Override
            public boolean Cast(Enemy enemy) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        assertFalse(testUnit.AttackVisit(player), "Unit's default AttackVisit(Player) should return false.");
    }

    @Test
    void testDefaultAttackVisitEnemy_ReturnsFalse() {
        Enemy enemy = new Enemy(10, "DummyEnemy", 100, 100, 10, 5, new Position(1, 0), "E", null, msg -> {}) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        assertFalse(testUnit.AttackVisit(enemy), "Unit's default AttackVisit(Enemy) should return false.");
    }
}