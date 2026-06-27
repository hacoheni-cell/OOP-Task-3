package tests.unitsTest;

import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Mage;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MageTest {
    private List<String> sentMessages;
    private int[] combatCallCount;
    private int[] attackCallCount;
    private double[] lastSpellDamage;
    private CombatSystem testCombatSystem;
    private Mage testMage;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        combatCallCount = new int[]{0};
        attackCallCount = new int[]{0};
        lastSpellDamage = new double[]{0.0};

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                combatCallCount[0]++;
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                attackCallCount[0]++;
                lastSpellDamage[0] = damage;
                return true;
            }
        };

        testMage = new Mage("DummyMage", 100, 20, 10, new Position(0, 0), testCombatSystem,
                100, 30, 50, 3, 5, "M", msg -> sentMessages.add(msg));
    }

    private Enemy createDummyEnemy(int health, int exp) {
        return new Enemy(exp, "DummyEnemy", health, health, 10, 5, new Position(1, 1), "E",
                testCombatSystem, msg -> sentMessages.add(msg)) {
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
    void testConstructor_InitializesCurrentManaCorrectly() {
        String description = testMage.Description();

        assertTrue(description.contains("Mana: 25/100"));
        assertEquals(5, testMage.GetRange());
    }

    @Test
    void testLevelUp_IncreasesMageSpecificStats() {
        testMage.LevelUp();

        String description = testMage.Description();

        assertTrue(description.contains("Mana: 62/150"));
        assertTrue(description.contains("Spell Power: 70"));
    }

    @Test
    void testGameTick_RegeneratesManaProperly() {
        testMage.LevelUp();
        testMage.GameTick();

        assertTrue(testMage.Description().contains("Mana: 64/150"));
    }

    @Test
    void testCastList_NotEnoughMana() {
        List<Unit> targets = new ArrayList<>();
        targets.add(createDummyEnemy(50, 10));

        int result = testMage.Cast(targets);

        assertEquals(0, result);
        assertTrue(sentMessages.contains("Cannot cast: Not enough mana."));
        assertEquals(0, attackCallCount[0]);
    }

    @Test
    void testCastList_SuccessWithMultipleHits() {
        testMage.SetExperience(150);

        Enemy target = createDummyEnemy(1000, 10);
        List<Unit> targets = new ArrayList<>(Arrays.asList(target, testMage));

        testMage.Cast(targets);

        assertTrue(sentMessages.contains("DummyMage cast Blizzard."));
        assertEquals(3, attackCallCount[0]);
        assertFalse(targets.contains(testMage));
    }

    @Test
    void testCastEnemy_AppliesSpellPowerAndGainsExperience() {
        Enemy enemy = createDummyEnemy(100, 50);
        enemy.SetHealthAmount(0);

        boolean hit = testMage.Cast(enemy);

        assertTrue(hit);
        assertEquals(50.0, lastSpellDamage[0]);
        assertTrue(testMage.Description().contains("Level: 2"));
        assertTrue(sentMessages.stream().anyMatch(msg -> msg.contains("DummyEnemy died") && msg.contains("gained 50 experience")));
    }
}