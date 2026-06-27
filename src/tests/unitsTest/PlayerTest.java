package tests.unitsTest;

import Game.Cell;
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

class PlayerTest {
    private List<String> sentMessages;
    private CombatSystem testCombatSystem;
    private int combatCallCount;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        combatCallCount = 0;

        testCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) {
                combatCallCount++;
                return 0;
            }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) {
                return true;
            }
        };

        testPlayer = createPlayer(100, 20, 5, new Position(0, 0));
    }

    private Player createPlayer(int healthPool, int attack, int defence, Position pos) {
        return new Player("DummyPlayer", healthPool, attack, defence, pos, "P",
                testCombatSystem, msg -> sentMessages.add(msg)) {
            @Override
            public int Cast(List<Unit> listOfUnits) { return 0; }
            @Override
            public boolean Cast(Enemy enemy) { return true; }
            @Override
            public int GetRange() { return 2; }
            @Override
            public void GameTick() {}
        };
    }

    private Enemy createDummyEnemy(int experience, boolean dead) {
        return new Enemy(experience, "DummyEnemy", 100, dead ? 0 : 100, 10, 5,
                new Position(1, 0), "E", testCombatSystem, msg -> sentMessages.add(msg)) {
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

    private Cell createCell(Position pos, boolean accepts) {
        return new Cell(pos, "F") {
            @Override
            public boolean Accept(Unit unit) { return accepts; }
        };
    }

    private GameContext createContext(boolean cellAccepts, List<Unit> unitsInRange) {
        return new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) {
                return createCell(new Position(p.getX() + x, p.getY() + y), cellAccepts);
            }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                return unitsInRange != null ? unitsInRange : new ArrayList<>();
            }
            @Override
            public Position getPlayerPos() { return testPlayer.getPos(); }
            @Override
            public void clearCell(Position p) {}
        };
    }

    @Test
    void testConstructor_InitializesLevelAndExperienceToDefaults() {
        assertTrue(testPlayer.Description().contains("Level: 1"), "playerLevel should start at 1.");
        assertTrue(testPlayer.Description().contains("Experience: 0/50"), "experience should start at 0, and threshold should be 50 * level 1.");
    }

    @Test
    void testConstructor_HealthAmountStartsAtHealthPool() {
        assertEquals(100, testPlayer.getHealthAmount(), "Constructor should set healthAmount equal to healthPool.");
    }

    @Test
    void testDescription_ContainsLevelAndExperience() {
        String description = testPlayer.Description();
        assertTrue(description.contains("DummyPlayer"), "Description should contain the base Unit description.");
        assertTrue(description.contains("Level:"), "Description should contain the Level field.");
        assertTrue(description.contains("Experience:"), "Description should contain the Experience field.");
    }

    @Test
    void testIsDead_VariousHealthStates() {
        assertFalse(testPlayer.isDead(), "Player should not be dead at full health.");

        testPlayer.SetHealthAmount(0);
        assertTrue(testPlayer.isDead(), "Player should be dead when health is exactly 0.");
    }

    @Test
    void testToString_AliveReturnsTileString() {
        assertEquals("P", testPlayer.toString(), "toString should return tileString while the player is alive.");
    }

    @Test
    void testToString_DeadReturnsX() {
        testPlayer.SetHealthAmount(0);
        assertEquals("X", testPlayer.toString(), "toString should return \"X\" once the player is dead.");
    }

    @Test
    void testAdvanceAccept_DelegatesToAdvanceVisitOnGivenUnit() {
        Enemy enemy = createDummyEnemy(10, false);
        boolean result = testPlayer.AdvanceAccept(enemy);

        assertFalse(result, "AdvanceAccept delegates to Enemy.AdvanceVisit(Player), which always returns false.");
        assertEquals(1, combatCallCount, "Enemy.AdvanceVisit should invoke combatUtiles.Combat exactly once.");
    }

    @Test
    void testAttackAccept_DelegatesToAttackVisitOnGivenUnit() {
        Enemy enemy = createDummyEnemy(10, false);
        boolean result = testPlayer.AttackAccept(enemy);

        assertTrue(result, "AttackAccept delegates to Enemy.AttackVisit(Player), which calls the enemy's own cast() and always returns true.");
    }

    @Test
    void testAdvanceVisit_EnemyNotDead_ReturnsFalseAndNoExperienceGained() {
        Enemy enemy = createDummyEnemy(10, false);
        boolean result = testPlayer.AdvanceVisit(enemy);

        assertFalse(result, "AdvanceVisit should return false when the enemy survives combat.");
        assertEquals(1, combatCallCount, "Combat should be invoked exactly once.");
        assertTrue(testPlayer.Description().contains("Experience: 0/50"), "No experience should be gained when the enemy is not dead.");
    }

    @Test
    void testAdvanceVisit_EnemyDies_ReturnsTrueSendsMessageAndGainsExperience() {
        Enemy enemy = createDummyEnemy(10, true);
        boolean result = testPlayer.AdvanceVisit(enemy);

        assertTrue(result, "AdvanceVisit should return true when the enemy dies.");
        assertEquals(1, sentMessages.size(), "A death message should be sent.");
        assertTrue(sentMessages.get(0).contains("died"), "Message should state that the enemy died.");
        assertTrue(testPlayer.Description().contains("Experience: 10/50"), "Player should gain the enemy's experience (10).");
    }

    @Test
    void testAttackVisit_DelegatesToCastEnemy() {
        Enemy enemy = createDummyEnemy(10, false);
        boolean result = testPlayer.AttackVisit(enemy);

        assertTrue(result, "AttackVisit(Enemy) should return whatever Cast(Enemy) returns (true, per the dummy implementation).");
    }

    @Test
    void testSetExperience_BelowThreshold_NoLevelUp() {
        testPlayer.SetExperience(40);

        assertTrue(testPlayer.Description().contains("Level: 1"), "Player should remain at level 1.");
        assertTrue(testPlayer.Description().contains("Experience: 40/50"), "Experience should be set to 40.");
        assertTrue(sentMessages.isEmpty(), "No level-up message should be sent.");
    }

    @Test
    void testSetExperience_AtThreshold_TriggersOneLevelUp() {
        testPlayer.SetExperience(50);

        assertTrue(testPlayer.Description().contains("Level: 2"), "Reaching exactly 50 experience should trigger a level up to level 2.");
        assertTrue(testPlayer.Description().contains("Experience: 0/100"),
                "After leveling up, leftover experience should be 0 (50 - 50*1), and the next threshold is 50*2=100.");
        assertEquals(1, sentMessages.size(), "Exactly one level-up message should be sent.");
        assertTrue(sentMessages.get(0).contains("reached level 2"), "Message should announce reaching level 2.");
    }

    @Test
    void testSetExperience_Negative_ClampsToZero() {
        testPlayer.SetExperience(-10);

        assertTrue(testPlayer.Description().contains("Experience: 0/50"), "Negative experience should be clamped to 0.");
        assertTrue(sentMessages.isEmpty(), "No level-up message should be sent when experience is clamped to 0.");
    }

    @Test
    void testLevelUp_IncreasesStatsAndHealsToFullPool() {
        testPlayer.SetHealthAmount(10);
        testPlayer.LevelUp();

        assertTrue(testPlayer.Description().contains("Level: 2"), "playerLevel should increment by 1.");
        assertEquals(120, testPlayer.getHealthAmount(), "healthAmount should be fully restored to the new healthPool (100 + 10*2 = 120).");
        assertEquals(28, testPlayer.getAttackPoints(), "attackPoints should increase by 4 * new level (4*2=8): 20 + 8 = 28.");
        assertEquals(7, testPlayer.getDefencePoints(), "defencePoints should increase by the new level (2): 5 + 2 = 7.");
    }

    @Test
    void testLevelUp_SendsLevelUpMessage() {
        testPlayer.LevelUp();

        assertEquals(1, sentMessages.size(), "LevelUp should send exactly one message.");
        assertTrue(sentMessages.get(0).contains("reached level 2"), "Message should mention the new level.");
    }

    @Test
    void testProcessInput_MoveAccepted_UpdatesPosition() {
        GameContext context = createContext(true, null);

        testPlayer.processInput("d", context);

        assertEquals(new Position(1, 0), testPlayer.getPos(), "Moving 'd' (right) on an accepting cell should update the player's position.");
    }

    @Test
    void testProcessInput_MoveRejected_PositionUnchanged() {
        Position originalPos = testPlayer.getPos();
        GameContext context = createContext(false, null);

        testPlayer.processInput("w", context);

        assertEquals(originalPos, testPlayer.getPos(), "Move should not happen when the target cell rejects entry.");
    }

    @Test
    void testProcessInput_CaseInsensitive() {
        GameContext context = createContext(true, null);

        testPlayer.processInput("D", context);

        assertEquals(new Position(1, 0), testPlayer.getPos(), "processInput should be case-insensitive (uppercase 'D' should still move right).");
    }

    @Test
    void testProcessInput_UnknownCommand_SendsErrorMessage() {
        GameContext context = createContext(true, null);

        testPlayer.processInput("z", context);

        assertEquals(1, sentMessages.size(), "An unknown command should send exactly one message.");
        assertTrue(sentMessages.get(0).contains("Unknown command"), "Message should indicate the command was not recognized.");
    }

    @Test
    void testProcessInput_QuitCommand_DoesNothingAndSendsNoMessage() {
        Position originalPos = testPlayer.getPos();
        GameContext context = createContext(true, null);

        testPlayer.processInput("q", context);

        assertEquals(originalPos, testPlayer.getPos(), "Passing the turn ('q') should not change the player's position.");
        assertTrue(sentMessages.isEmpty(), "Passing the turn should not send any message.");
    }
}