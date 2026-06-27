package tests.combatTests;

import Game.Position;
import Units.Warrior;
import combat.DefaultCombat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCombatTest {
    private DefaultCombat combatSystem;
    private List<String> sentMessages;

    @BeforeEach
    void setUp() {
        DefaultCombat.INSTANCE = null;
        sentMessages = new ArrayList<>();
        combatSystem = DefaultCombat.getInstance(msg -> sentMessages.add(msg));

        try {
            Field randomField = DefaultCombat.class.getDeclaredField("random");
            randomField.setAccessible(true);
            randomField.set(combatSystem, new Random() {
                @Override
                public int nextInt(int bound) {
                    return bound - 1;
                }
            });
        } catch (Exception e) {
            fail("Failed to inject mock Random via reflection.");
        }
    }

    @AfterEach
    void tearDown() {
        DefaultCombat.INSTANCE = null;
        sentMessages.clear();
        combatSystem = null;
    }

    private Warrior createDummyUnit(String name, int health, int attack, int defense) {
        return new Warrior(name, health, attack, defense, new Position(0, 0), combatSystem, "@", 3, msg -> {}) {
            private int currentHealth = health;

            @Override
            public int getHealthAmount() { return currentHealth; }

            @Override
            public void SetHealthAmount(int amount) { this.currentHealth = amount; }

            @Override
            public int getAttackPoints() { return attack; }

            @Override
            public int getDefencePoints() { return defense; }

            @Override
            public String getName() { return name; }

            @Override
            public String Description() { return name + " dummy description"; }
        };
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        DefaultCombat instance1 = DefaultCombat.getInstance(msg -> {});
        DefaultCombat instance2 = DefaultCombat.getInstance(msg -> {});

        assertSame(instance1, instance2);
        assertSame(combatSystem, instance1);
    }

    @Test
    void testCombat_AttackerDealsDamage() {
        Warrior attacker = createDummyUnit("Attacker", 100, 20, 0);
        Warrior defender = createDummyUnit("Defender", 100, 0, 5);

        int result = combatSystem.Combat(attacker, defender);

        assertEquals(15, result);
        assertEquals(85, defender.getHealthAmount());
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("dealt 15 damage")));
    }

    @Test
    void testCombat_DefenderBlocksAllDamage() {
        Warrior attacker = createDummyUnit("WeakAttacker", 100, 5, 0);
        Warrior defender = createDummyUnit("TankDefender", 100, 0, 15);

        int result = combatSystem.Combat(attacker, defender);

        assertEquals(0, result);
        assertEquals(100, defender.getHealthAmount());
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("dealt 0 damage")));
    }

    @Test
    void testCombat_DefenderDies_ReturnsMinusOne() {
        Warrior attacker = createDummyUnit("StrongAttacker", 100, 50, 0);
        Warrior defender = createDummyUnit("FragileDefender", 10, 0, 5);

        int result = combatSystem.Combat(attacker, defender);

        assertEquals(-1, result);
        assertTrue(defender.getHealthAmount() <= 0);
    }

    @Test
    void testAttack_SpecialAbilityDealsDamage() {
        Warrior defender = createDummyUnit("Defender", 100, 0, 10);

        boolean isDead = combatSystem.Attack(defender, 30.0, "Hero");

        assertFalse(isDead);
        assertEquals(80, defender.getHealthAmount());
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("Hero dealt 20 damage")));
    }

    @Test
    void testAttack_SpecialAbilityBlocked() {
        Warrior defender = createDummyUnit("TankDefender", 100, 0, 20);

        boolean isDead = combatSystem.Attack(defender, 15.0, "Hero");

        assertFalse(isDead);
        assertEquals(100, defender.getHealthAmount());
        assertTrue(sentMessages.stream().anyMatch(m -> m.contains("fully blocked")));
    }

    @Test
    void testAttack_SpecialAbilityKillsDefender() {
        Warrior defender = createDummyUnit("FragileDefender", 20, 0, 5);

        boolean isDead = combatSystem.Attack(defender, 50.0, "Hero");

        assertTrue(isDead);
        assertTrue(defender.getHealthAmount() <= 0);
    }
}