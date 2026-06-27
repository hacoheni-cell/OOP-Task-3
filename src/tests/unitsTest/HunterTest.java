package tests.unitsTest;

import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Hunter;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HunterTest {
    private static final int RANGE = 4;

    private List<String> sentMessages;
    private CombatSystem testCombatSystem;
    private boolean combatAttackResult;
    private Hunter testHunter;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        combatAttackResult = true;

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                return combatAttackResult;
            }
        };

        testHunter = new Hunter("DummyHunter", 100, 20, 5, new Position(0, 0),
                testCombatSystem, RANGE, "H", msg -> sentMessages.add(msg));
    }

    private Enemy createDummyEnemy(int experience, boolean dead) {
        return new Enemy(experience, "DummyEnemy", 100, dead ? 0 : 100, 10, 5,
                new Position(2, 0), "X", testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };
    }

    @Test
    void testConstructor_InitializesRangeArrowsAndTicks() {
        assertEquals(RANGE, testHunter.GetRange(), "GetRange should return the constructor's range value.");
        assertTrue(testHunter.Description().contains("Arrows: 10"),
                "arrowsCount should start at 10 * playerLevel (10 * 1 = 10).");
        assertTrue(testHunter.Description().contains("Ticks: 0/10"),
                "ticksCount should start at 0.");
    }

    @Test
    void testGetRange_ReturnsStoredRange() {
        assertEquals(RANGE, testHunter.GetRange());
    }

    @Test
    void testDescription_ContainsArrowsAndTicks() {
        String description = testHunter.Description();
        assertTrue(description.contains("DummyHunter"), "Description should still contain the base Unit description.");
        assertTrue(description.contains("Arrows:"), "Description should contain the Arrows field.");
        assertTrue(description.contains("Ticks:"), "Description should contain the Ticks field.");
    }

    @Test
    void testGameTick_IncrementsTicksCountBelowThreshold() {
        testHunter.GameTick();
        assertTrue(testHunter.Description().contains("Ticks: 1/10"), "ticksCount should increment by 1 on GameTick.");
        assertTrue(testHunter.Description().contains("Arrows: 10"), "arrowsCount should not change while ticksCount < 10.");
    }

    @Test
    void testGameTick_ResetsTicksAndAddsArrowsAtThreshold() {
        for (int i = 0; i < 11; i++) {
            testHunter.GameTick();
        }
        assertTrue(testHunter.Description().contains("Ticks: 0/10"),
                "ticksCount should reset to 0 on the 11th call, since the reset check happens before incrementing.");
        assertTrue(testHunter.Description().contains("Arrows: 11"),
                "arrowsCount should increase by playerLevel (1) once ticksCount reaches 10: 10 + 1 = 11.");
    }

    @Test
    void testCastList_NoArrowsLeft_SendsMessageAndReturnsZero() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(10, false));

        for (int i = 0; i < 10; i++) {
            testHunter.Cast(targets);
        }
        sentMessages.clear();

        int result = testHunter.Cast(targets);

        assertEquals(0, result, "Cast should return 0 when there are no arrows left.");
        assertEquals(1, sentMessages.size(), "Cast should send exactly one message when out of arrows.");
        assertTrue(sentMessages.get(0).contains("No arrows left"), "Message should state that there are no arrows left.");
    }

    @Test
    void testCastList_EmptyTargetList_SendsMessageAndReturnsZero() {
        List<Unit> targets = new ArrayList<>();

        int result = testHunter.Cast(targets);

        assertEquals(0, result, "Cast should return 0 when there are no enemies in range.");
        assertTrue(sentMessages.get(sentMessages.size() - 1).contains("no enemies in range"),
                "Message should state that there are no enemies in range.");
    }

    @Test
    void testCastList_AttacksClosestTargetAndConsumesArrow() {
        Enemy closeEnemy = createDummyEnemy(10, false);
        Enemy farEnemy = new Enemy(10, "FarEnemy", 100, 100, 10, 5,
                new Position(10, 10), "X", testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        List<Unit> targets = new ArrayList<>();
        targets.add(farEnemy);
        targets.add(closeEnemy);

        int result = testHunter.Cast(targets);

        assertEquals(1, result, "Cast should return 1 when an arrow is successfully fired.");
        assertTrue(testHunter.Description().contains("Arrows: 9"), "arrowsCount should decrease by 1 after a successful cast.");
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("DummyEnemy")),
                "Message should mention the closest enemy's name (DummyEnemy, not FarEnemy).");
    }

    @Test
    void testCastEnemy_HitAndAliveEnemy_NoExperienceMessage() {
        Enemy enemy = createDummyEnemy(15, false);
        boolean result = testHunter.Cast(enemy);

        assertTrue(result, "Cast(Enemy) should return whatever combatUtiles.Attack returns (true in this case).");
        assertTrue(sentMessages.isEmpty(), "No death message should be sent when the enemy is not dead.");
    }

    @Test
    void testCastEnemy_KillsEnemy_SendsMessageAndGainsExperience() {
        Enemy enemy = createDummyEnemy(15, true);
        boolean result = testHunter.Cast(enemy);

        assertTrue(result, "Cast(Enemy) should return the combat result (true).");
        assertEquals(1, sentMessages.size(), "A death message should be sent when the enemy dies.");
        assertTrue(sentMessages.get(0).contains("died"), "Message should state that the enemy died.");
        assertTrue(sentMessages.get(0).contains("15"), "Message should mention the experience gained (15).");
    }

    @Test
    void testCastEnemy_MissedAttack_ReturnsFalse() {
        combatAttackResult = false;
        Enemy enemy = createDummyEnemy(15, false);

        boolean result = testHunter.Cast(enemy);

        assertFalse(result, "Cast(Enemy) should return false when combatUtiles.Attack reports a miss.");
    }
}