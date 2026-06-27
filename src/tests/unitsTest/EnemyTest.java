package tests.unitsTest;

import Game.GameContext;
import Game.Position;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    private static final int EXPERIENCE = 25;

    private List<String> sentMessages;
    private int[] combatCallCount;
    private CombatSystem testCombatSystem;
    private Enemy testEnemy;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        combatCallCount = new int[]{0};

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                combatCallCount[0]++;
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                return false;
            }
        };

        testEnemy = new Enemy(EXPERIENCE, "DummyEnemy", 100, 100, 30, 10,
                new Position(0, 0), "E", testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public void cast(Unit unit) {
                int damage = Math.max(0, this.getAttackPoints() - unit.getDefencePoints());
                unit.SetHealthAmount(unit.getHealthAmount() - damage);
            }
            @Override
            public void takeTurn(GameContext gameContext) {}
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };
    }

    private Player createDummyPlayer() {
        return new Player("DummyPlayer", 100, 20, 5, new Position(1, 0), "P",
                testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public int Cast(List<Unit> listOfUnits) { return 0; }
            @Override
            public boolean Cast(Enemy enemy) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };
    }

    @Test
    void testConstructor_SetsExperience() {
        assertEquals(EXPERIENCE, testEnemy.getExperience(), "Constructor should store the given experience value.");
    }

    @Test
    void testGetExperience_ReturnsStoredValue() {
        assertEquals(EXPERIENCE, testEnemy.getExperience(), "getExperience should return the experience field as-is.");
    }

    @Test
    void testDescription_AppendsExperienceToParentDescription() {
        String description = testEnemy.Description();
        assertTrue(description.contains("DummyEnemy"), "Description should still contain the base Unit description.");
        assertTrue(description.contains("Experience: " + EXPERIENCE), "Description should append the Experience field.");
    }

    @Test
    void testAttackAccept_DelegatesToAttackVisitOnGivenUnit() {
        Player player = createDummyPlayer();
        boolean result = testEnemy.AttackAccept(player);

        assertFalse(result, "AttackAccept delegates to Player.AttackVisit(Enemy), which calls Cast(Enemy); the dummy player's Cast(Enemy) returns false.");
    }

    @Test
    void testAdvanceAccept_DelegatesToAdvanceVisitOnGivenUnit() {
        Player player = createDummyPlayer();
        boolean result = testEnemy.AdvanceAccept(player);

        assertFalse(result, "AdvanceAccept delegates to Player.AdvanceVisit(Enemy): Combat is invoked but the mock combat system doesn't kill the enemy, so it returns false.");
        assertEquals(1, combatCallCount[0], "Combat should have been invoked exactly once through the double dispatch chain.");
    }

    @Test
    void testAttackVisit_CastsOnPlayerAndReturnsTrue() {
        Player player = createDummyPlayer();
        boolean result = testEnemy.AttackVisit(player);

        assertTrue(result, "AttackVisit(Player) should always return true.");
        assertEquals(75, player.getHealthAmount(),
                "cast should have been invoked on the player: 30 attack - 5 defence = 25 damage, 100 - 25 = 75.");
    }

    @Test
    void testAdvanceVisit_InvokesCombatAndReturnsFalse() {
        Player player = createDummyPlayer();
        boolean result = testEnemy.AdvanceVisit(player);

        assertFalse(result, "AdvanceVisit(Player) should always return false, regardless of combat outcome.");
        assertEquals(1, combatCallCount[0], "AdvanceVisit should invoke combatUtiles.Combat(this, player) exactly once.");
    }
}