package tests.unitsTest;

import combat.CombatSystem;
import Game.Cell;
import Game.GameContext;
import businessLayer.MessageCallback;
import Game.Position;
import Units.Boss;
import Units.Player;
import Units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BossTest {
    private static final int VISION_RANGE = 5;
    private static final int ABILITY_COOLDOWN = 3;

    private List<String> sentMessages;
    private MessageCallback testMessageCallback;
    private Boss testBoss;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        testMessageCallback = message -> sentMessages.add(message);

        testBoss = new Boss(VISION_RANGE, 50, "DummyBoss", 200, 200, 30, 10,
                new Position(0, 0), "B", null, ABILITY_COOLDOWN, testMessageCallback);
    }

    private Unit createDummyTarget(int healthPool, int healthAmount, int defencePoints) {
        return new Unit("DummyTarget", healthPool, healthAmount, 0, defencePoints,
                new Position(1, 0), "D", null, msg -> {}) {
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

    private Cell createCell(Position pos, boolean accepts) {
        return new Cell(pos, "F") {
            @Override
            public boolean Accept(Unit unit) { return accepts; }
        };
    }

    @Test
    void testGetRange_ReturnsVisionRange() {
        assertEquals(VISION_RANGE, testBoss.GetRange(), "GetRange should return the Boss's visionRange.");
    }

    @Test
    void testDescription_ContainsVisionRangeAndCooldown() {
        String description = testBoss.Description();
        assertTrue(description.contains("Vision Range: " + VISION_RANGE),
                "Description should contain the Boss's vision range.");
        assertTrue(description.contains("Cooldown: 0/" + ABILITY_COOLDOWN),
                "Description should contain the current and max cooldown.");
    }

    @Test
    void testGameTick_DecrementsRemainingCooldown() {
        Unit target = createDummyTarget(100, 100, 0);
        testBoss.cast(target);
        assertTrue(testBoss.Description().contains("Cooldown: 0/" + ABILITY_COOLDOWN),
                "remainingCooldown is only set inside takeTurn, so it should still be 0 here.");

        GameContext context = new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) { return createCell(p, true); }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                List<Unit> units = new ArrayList<>();
                units.add(target);
                return units;
            }
            @Override
            public Position getPlayerPos() { return new Position(0, 0); }
            @Override
            public void clearCell(Position p) {}
        };

        testBoss.takeTurn(context);
        assertTrue(testBoss.Description().contains("Cooldown: " + ABILITY_COOLDOWN + "/" + ABILITY_COOLDOWN),
                "After casting in takeTurn, remainingCooldown should equal abilityCooldown.");

        testBoss.GameTick();
        assertTrue(testBoss.Description().contains("Cooldown: " + (ABILITY_COOLDOWN - 1) + "/" + ABILITY_COOLDOWN),
                "GameTick should decrement remainingCooldown by 1.");
    }

    @Test
    void testGameTick_DoesNotGoBelowZero() {
        testBoss.GameTick();
        assertTrue(testBoss.Description().contains("Cooldown: 0/" + ABILITY_COOLDOWN),
                "remainingCooldown should not go below 0 when it is already 0.");
    }

    @Test
    void testCast_DealsDamageReducedByDefence() {
        Unit target = createDummyTarget(100, 100, 10);
        testBoss.cast(target);
        assertEquals(80, target.getHealthAmount(),
                "Damage dealt should be attackPoints (30) minus target's defencePoints (10) = 20.");
    }

    @Test
    void testCast_DefenceHigherThanAttack_DealsNoDamage() {
        Unit target = createDummyTarget(100, 100, 999);
        testBoss.cast(target);
        assertEquals(100, target.getHealthAmount(),
                "Damage should be clamped at 0 when defence exceeds attack points.");
    }

    @Test
    void testCast_SendsMessageThroughCallback() {
        Unit target = createDummyTarget(100, 100, 5);
        testBoss.cast(target);
        assertEquals(1, sentMessages.size(), "cast should send exactly one message.");
        assertTrue(sentMessages.get(0).contains("DummyBoss"), "Message should mention the Boss's name.");
        assertTrue(sentMessages.get(0).contains("DummyTarget"), "Message should mention the target's name.");
    }

    @Test
    void testTakeTurn_PlayerInRangeAndCooldownReady_CastsOnTargetsInRange() {
        Unit target = createDummyTarget(100, 100, 0);
        GameContext context = new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) { return createCell(p, true); }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                List<Unit> units = new ArrayList<>();
                units.add(target);
                return units;
            }
            @Override
            public Position getPlayerPos() { return new Position(1, 0); }
            @Override
            public void clearCell(Position p) {}
        };

        testBoss.takeTurn(context);

        assertEquals(70, target.getHealthAmount(),
                "Target in range should take damage (30 attack - 0 defence) when ability is cast.");
        assertTrue(testBoss.Description().contains("Cooldown: " + ABILITY_COOLDOWN + "/" + ABILITY_COOLDOWN),
                "remainingCooldown should be set to abilityCooldown after casting.");
    }

    @Test
    void testTakeTurn_PlayerInRangeButOnCooldown_MovesTowardsPlayerInsteadOfCasting() {
        Unit target = createDummyTarget(100, 100, 0);
        final boolean[] cellRequested = {false};
        GameContext context = new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) {
                cellRequested[0] = true;
                return createCell(new Position(p.getX() + x, p.getY() + y), true);
            }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                List<Unit> units = new ArrayList<>();
                units.add(target);
                return units;
            }
            @Override
            public Position getPlayerPos() { return new Position(3, 0); }
            @Override
            public void clearCell(Position p) {}
        };

        // First call puts the Boss on cooldown without moving (player out of range check uses distance < visionRange).
        testBoss.takeTurn(context);
        cellRequested[0] = false;

        // Second call: still in range, but now on cooldown -> should move towards the player.
        testBoss.takeTurn(context);

        assertTrue(cellRequested[0], "Boss should query for a cell to move into when on cooldown and player is in range.");
        assertEquals(new Position(1, 0), testBoss.getPos(),
                "Boss should step one tile towards the player along the larger axis of distance.");
    }

    @Test
    void testTakeTurn_PlayerOutOfRange_PerformsRandomMove() {
        final boolean[] cellRequested = {false};
        GameContext context = new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) {
                cellRequested[0] = true;
                return createCell(new Position(p.getX() + x, p.getY() + y), true);
            }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                return new ArrayList<>();
            }
            @Override
            public Position getPlayerPos() { return new Position(100, 100); }
            @Override
            public void clearCell(Position p) {}
        };

        testBoss.takeTurn(context);

        assertTrue(cellRequested[0], "Boss should query for a cell to move into when the player is out of vision range.");
    }

    @Test
    void testTakeTurn_TargetCellRejectsMove_BossStaysInPlace() {
        Position originalPos = testBoss.getPos();
        GameContext context = new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) {
                return createCell(new Position(p.getX() + x, p.getY() + y), false);
            }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                return new ArrayList<>();
            }
            @Override
            public Position getPlayerPos() { return new Position(100, 100); }
            @Override
            public void clearCell(Position p) {}
        };

        testBoss.takeTurn(context);

        assertEquals(originalPos, testBoss.getPos(),
                "Boss should not move when the target cell rejects entry (Accept returns false).");
    }
}