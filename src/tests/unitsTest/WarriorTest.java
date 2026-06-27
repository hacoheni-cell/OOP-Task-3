package tests.unitsTest;

import Game.Position;
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

class WarriorTest {
    private List<String> sentMessages;
    private CombatSystem testCombatSystem;
    private Warrior testWarrior;
    private double[] lastSpellDamage;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        lastSpellDamage = new double[]{0.0};

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                lastSpellDamage[0] = damage;
                return true;
            }
        };

        testWarrior = new Warrior("DummyWarrior", 100, 20, 5, new Position(0, 0),
                testCombatSystem, "W", 3, msg -> sentMessages.add(msg));
    }

    @AfterEach
    void tearDown() {
        sentMessages.clear();
        testWarrior = null;
        testCombatSystem = null;
        lastSpellDamage = null;
    }

    private Enemy createDummyEnemy(int health, int exp, boolean isDead) {
        return new Enemy(exp, "DummyEnemy", health, health, 10, 5, new Position(1, 1), "E",
                testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public void cast(Unit unit) {}
            @Override
            public void takeTurn(Game.GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
            @Override
            public boolean isDead() {
                return isDead;
            }
        };
    }

    @Test
    void testConstructorAndDescription() {
        String description = testWarrior.Description();

        assertTrue(description.contains("Cooldown: 0/3"));
        assertEquals(3, testWarrior.GetRange());
    }

    @Test
    void testGameTick_DecreasesCooldown() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(50, 10, false));
        testWarrior.Cast(targets);

        assertTrue(testWarrior.Description().contains("Cooldown: 3/3"));

        testWarrior.GameTick();

        assertTrue(testWarrior.Description().contains("Cooldown: 2/3"));
    }

    @Test
    void testCastList_OnCooldown() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(50, 10, false));
        testWarrior.Cast(targets);

        sentMessages.clear();
        int result = testWarrior.Cast(targets);

        assertEquals(0, result);
        assertTrue(sentMessages.contains("Ability is on cooldown: 3 turns remaining."));
    }

    @Test
    void testCastList_NoEnemiesInRange_DoesNotHealOrResetCooldown() {
        testWarrior.SetHealthAmount(10);

        List<Unit> targets = new ArrayList<>();
        targets.add(testWarrior);

        int result = testWarrior.Cast(targets);

        assertEquals(0, result);
        assertTrue(sentMessages.contains("There are no enemies in range."));
        assertEquals(10, testWarrior.getHealthAmount());
        assertTrue(testWarrior.Description().contains("Cooldown: 0/3"));
    }

    @Test
    void testCastList_SuccessWithHealingAndCooldown() {
        testWarrior.SetHealthAmount(10);

        List<Unit> targets = new ArrayList<>();
        targets.add(testWarrior);
        targets.add(createDummyEnemy(50, 10, false));

        int result = testWarrior.Cast(targets);

        assertEquals(1, result);
        assertEquals(60, testWarrior.getHealthAmount());
        assertTrue(sentMessages.contains("DummyWarrior used Avenger's Strike, healing for 50."));
        assertTrue(testWarrior.Description().contains("Cooldown: 3/3"));
        assertFalse(targets.contains(testWarrior));
    }

    @Test
    void testCastEnemy_HitsAndGainsXP() {
        Enemy enemy = createDummyEnemy(10, 50, true);

        boolean hit = testWarrior.Cast(enemy);

        assertTrue(hit);
        assertEquals(10.0, lastSpellDamage[0]);
        assertTrue(sentMessages.contains("DummyEnemy died. DummyWarrior gained 50 experience."));
    }

    @Test
    void testLevelUp_ResetsCooldownAndIncreasesStats() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(50, 10, false));
        testWarrior.Cast(targets);

        testWarrior.LevelUp();

        assertTrue(testWarrior.Description().contains("Cooldown: 0/3"));
    }
}