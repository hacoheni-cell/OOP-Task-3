package UnitsTests;

import Combat_System.CombatSystem;
import Game.MessageCallback;
import Game.Position;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}