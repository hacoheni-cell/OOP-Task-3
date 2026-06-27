package tests.unitsTest;

import Game.Cell;
import Game.GameContext;
import Game.Position;
import Units.Monster;
import Units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
    private static final int VISION_RANGE = 5;

    private Monster testMonster;

    @BeforeEach
    void setUp() {
        testMonster = new Monster(VISION_RANGE, 20, "DummyMonster", 100, 100, 25, 10,
                new Position(0, 0), "M", null, msg -> {});
    }

    private Cell createCell(Position pos, boolean accepts) {
        return new Cell(pos, "F") {
            @Override
            public boolean Accept(Unit unit) { return accepts; }
        };
    }

    private GameContext createContext(Position playerPos, boolean cellAccepts, boolean[] cellRequested) {
        return new GameContext() {
            @Override
            public Cell getCell(Position p, int x, int y) {
                if (cellRequested != null) {
                    cellRequested[0] = true;
                }
                return createCell(new Position(p.getX() + x, p.getY() + y), cellAccepts);
            }
            @Override
            public List<Unit> getUnitsInRange(Position position, double range) {
                return new ArrayList<>();
            }
            @Override
            public Position getPlayerPos() { return playerPos; }
            @Override
            public void clearCell(Position p) {}
        };
    }

    @Test
    void testGetRange_ReturnsVisionRange() {
        assertEquals(VISION_RANGE, testMonster.GetRange(), "GetRange should return the Monster's visionRange.");
    }

    @Test
    void testDescription_ContainsVisionRange() {
        String description = testMonster.Description();
        assertTrue(description.contains("DummyMonster"), "Description should contain the base Unit description.");
        assertTrue(description.contains("Vision Range: " + VISION_RANGE), "Description should contain the Monster's vision range.");
    }

    @Test
    void testToString_ReturnsTileString() {
        assertEquals("M", testMonster.toString(), "toString should return the tileString passed to the constructor.");
    }

    @Test
    void testGameTick_DoesNothing() {
        assertDoesNotThrow(() -> testMonster.GameTick(), "GameTick should be a no-op for Monster and never throw.");
    }

    @Test
    void testCast_DoesNothing() {
        Unit target = new Unit("DummyTarget", 100, 100, 0, 0, new Position(1, 0), "D", null, msg -> {}) {
            @Override
            public boolean AttackAccept(Unit unit) { return false; }
            @Override
            public boolean AdvanceAccept(Unit unit) { return false; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };

        testMonster.cast(target);

        assertEquals(100, target.getHealthAmount(), "cast should be a no-op for Monster; target's health should remain unchanged.");
    }

    @Test
    void testTakeTurn_PlayerInRange_MovesTowardsPlayer() {
        GameContext context = createContext(new Position(3, 0), true, null);

        testMonster.takeTurn(context);

        assertEquals(new Position(1, 0), testMonster.getPos(),
                "Monster should step one tile towards the player along the larger axis of distance.");
    }

    @Test
    void testTakeTurn_PlayerOutOfRange_PerformsRandomMove() {
        boolean[] cellRequested = {false};
        GameContext context = createContext(new Position(100, 100), true, cellRequested);

        testMonster.takeTurn(context);

        assertTrue(cellRequested[0], "Monster should query for a cell to move into when the player is out of vision range.");
    }

    @Test
    void testTakeTurn_TargetCellRejectsMove_MonsterStaysInPlace() {
        Position originalPos = testMonster.getPos();
        GameContext context = createContext(new Position(3, 0), false, null);

        testMonster.takeTurn(context);

        assertEquals(originalPos, testMonster.getPos(),
                "Monster should not move when the target cell rejects entry (Accept returns false).");
    }

    @Test
    void testTakeTurn_PlayerExactlyAtVisionRangeBoundary_PerformsRandomMove() {
        // distance == visionRange, and the condition requires distance < visionRange, so this should NOT move toward the player.
        boolean[] cellRequested = {false};
        GameContext context = createContext(new Position(VISION_RANGE, 0), true, cellRequested);

        testMonster.takeTurn(context);

        assertTrue(cellRequested[0], "A cell should still be requested for the random move fallback.");
    }
}