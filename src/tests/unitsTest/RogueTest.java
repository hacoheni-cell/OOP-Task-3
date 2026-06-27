package tests.unitsTest;

import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Rogue;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RogueTest {
    private static final int COST = 30;

    private List<String> sentMessages;
    private CombatSystem testCombatSystem;
    private boolean combatAttackResult;
    private Rogue testRogue;

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

        testRogue = new Rogue("DummyRogue", 100, 20, 5, new Position(0, 0),
                testCombatSystem, "R", COST, msg -> sentMessages.add(msg));
    }

    private Enemy createDummyEnemy(Position pos, int experience, boolean dead) {
        return new Enemy(experience, "DummyEnemy", 100, dead ? 0 : 100, 10, 5,
                pos, "X", testCombatSystem, msg -> sentMessages.add(msg)) {
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
    void testConstructor_InitializesEnergyToFull() {
        assertTrue(testRogue.Description().contains("Energy: 100/100"), "currentEnergy should start at 100.");
    }

    @Test
    void testGetRange_ReturnsAttackRange() {
        assertEquals(2, testRogue.GetRange(), "GetRange should return the fixed attackRange of 2.");
    }

    @Test
    void testDescription_ContainsEnergy() {
        String description = testRogue.Description();
        assertTrue(description.contains("DummyRogue"), "Description should still contain the base Unit/Player description.");
        assertTrue(description.contains("Energy:"), "Description should contain the Energy field.");
    }

    @Test
    void testGameTick_AddsEnergyBelowCap() {
        for (int i = 0; i < 3; i++) {
            testRogue.Cast(new ArrayList<>(List.of(createDummyEnemy(new Position(1, 0), 1, false))));
        }
        // currentEnergy is now 100 - 3*30 = 10
        testRogue.GameTick();

        assertTrue(testRogue.Description().contains("Energy: 20/100"), "GameTick should add 10 energy: 10 + 10 = 20.");
    }

    @Test
    void testGameTick_CapsEnergyAt100() {
        testRogue.GameTick();

        assertTrue(testRogue.Description().contains("Energy: 100/100"), "GameTick should not raise energy above the 100 cap.");
    }

    @Test
    void testCastList_NotEnoughEnergy_SendsMessageAndReturnsZero() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(new Position(1, 0), 5, false));

        // Drain energy below COST (30): 100 -> 70 -> 40 -> 10, each cast costs 30.
        for (int i = 0; i < 3; i++) {
            testRogue.Cast(new ArrayList<>(targets));
        }
        sentMessages.clear();

        int result = testRogue.Cast(new ArrayList<>(targets));

        assertEquals(0, result, "Cast should return 0 when there is not enough energy.");
        assertEquals(1, sentMessages.size(), "Cast should send exactly one message when energy is insufficient.");
        assertTrue(sentMessages.get(0).contains("Not enough energy"), "Message should state that there is not enough energy.");
        assertTrue(testRogue.Description().contains("Energy: 10/100"), "Energy should remain unchanged (10) when the cast fails.");
    }

    @Test
    void testCastList_EnoughEnergy_ConsumesEnergyAndAttacksAllTargets() {
        Enemy enemy1 = createDummyEnemy(new Position(1, 0), 5, false);
        Enemy enemy2 = createDummyEnemy(new Position(2, 0), 5, false);
        List<Unit> targets = new ArrayList<>(List.of(enemy1, enemy2));

        int result = testRogue.Cast(targets);

        assertEquals(0, result, "Cast(List) always returns 0, regardless of outcome.");
        assertTrue(testRogue.Description().contains("Energy: 70/100"), "currentEnergy should decrease by cost (30): 100 - 30 = 70.");
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Fan of Knives")), "A message announcing the cast should be sent.");
    }

    @Test
    void testCastList_RemovesSelfFromTargetList() {
        List<Unit> targets = new ArrayList<>();
        targets.add(testRogue);
        targets.add(createDummyEnemy(new Position(1, 0), 5, false));

        assertDoesNotThrow(() -> testRogue.Cast(targets),
                "Cast should remove the Rogue itself from the target list before attacking, and not throw.");
    }

    @Test
    void testCastList_EmptyTargetList_DoesNotThrow() {
        List<Unit> targets = new ArrayList<>();

        int result = testRogue.Cast(targets);

        assertEquals(0, result, "Cast should still return 0 with an empty target list.");
        assertTrue(testRogue.Description().contains("Energy: 70/100"), "Energy should still be consumed even if there are no targets to hit.");
    }

    @Test
    void testCastEnemy_HitAndAliveEnemy_NoExperienceMessage() {
        Enemy enemy = createDummyEnemy(new Position(1, 0), 15, false);
        boolean result = testRogue.Cast(enemy);

        assertTrue(result, "Cast(Enemy) should return whatever combatUtiles.Attack returns (true in this case).");
        assertTrue(sentMessages.isEmpty(), "No death message should be sent when the enemy is not dead.");
    }

    @Test
    void testCastEnemy_KillsEnemy_SendsMessageAndGainsExperience() {
        Enemy enemy = createDummyEnemy(new Position(1, 0), 15, true);
        boolean result = testRogue.Cast(enemy);

        assertTrue(result, "Cast(Enemy) should return the combat result (true).");
        assertEquals(1, sentMessages.size(), "A death message should be sent when the enemy dies.");
        assertTrue(sentMessages.get(0).contains("died"), "Message should state that the enemy died.");
        assertTrue(sentMessages.get(0).contains("15"), "Message should mention the experience gained (15).");
    }

    @Test
    void testCastEnemy_MissedAttack_ReturnsFalse() {
        combatAttackResult = false;
        Enemy enemy = createDummyEnemy(new Position(1, 0), 15, false);

        boolean result = testRogue.Cast(enemy);

        assertFalse(result, "Cast(Enemy) should return false when combatUtiles.Attack reports a miss.");
    }

    @Test
    void testLevelUp_ResetsEnergyAndAddsExtraAttack() {
        testRogue.GameTick(); // no-op at full energy, just to confirm baseline
        testRogue.LevelUp();

        assertTrue(testRogue.Description().contains("Energy: 100/100"), "LevelUp should reset currentEnergy to 100.");
        assertEquals(34, testRogue.getAttackPoints(),
                "attackPoints after LevelUp: super.LevelUp adds 4*2=8 (20->28), then Rogue adds 3*2=6 (28->34).");
    }
}