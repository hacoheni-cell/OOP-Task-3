package tests.gameTests;

import Game.Floor;
import Game.GameBoard;
import Game.Position;
import Units.Warrior;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    private GameBoard board;
    private CombatSystem dummyCombatSystem;

    @BeforeEach
    void setUp() {
        // Create a 5x5 board for testing
        board = new GameBoard(5, 5);

        // Dummy combat system to satisfy Unit/Warrior constructor
        dummyCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) { return 0; }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) { return false; }
        };
    }

    @AfterEach
    void tearDown() {
        board = null;
        dummyCombatSystem = null;
    }

    // Helper method to create a dummy unit for our cells
    private Warrior createDummyUnit(Position p) {
        return new Warrior("Dummy", 100, 10, 10, p, dummyCombatSystem, "@", 3, msg -> {});
    }

    @Test
    void testConstructorAndDimensions() {
        assertEquals(5, board.getWidth(), "Board width should be initialized correctly.");
        assertEquals(5, board.getHeight(), "Board height should be initialized correctly.");
    }

    @Test
    void testSetAndGetCell() {
        Position pos = new Position(2, 3);
        Floor floorCell = new Floor(pos, null);

        board.setCell(pos.getX(), pos.getY(), floorCell);

        assertEquals(floorCell, board.getCell(2, 3), "getCell should return the exact cell set by setCell.");
        assertNull(board.getCell(0, 0), "Uninitialized cells should be null.");
    }

    @Test
    void testGetUnitsInRange_ExactRangeAndBoundaries() {
        // Populate a 3x3 section of the board
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Position p = new Position(x, y);
                Floor floor = new Floor(p, null);
                board.setCell(x, y, floor);
            }
        }

        // Place Unit A at center (1, 1)
        Position posA = new Position(1, 1);
        Warrior unitA = createDummyUnit(posA);
        ((Floor) board.getCell(1, 1)).setOccupant(unitA);

        // Place Unit B at (2, 1) - Distance 1.0 from center
        Position posB = new Position(2, 1);
        Warrior unitB = createDummyUnit(posB);
        ((Floor) board.getCell(2, 1)).setOccupant(unitB);

        // Place Unit C at (0, 0) - Distance Math.sqrt(2) ~ 1.414 from center
        Position posC = new Position(0, 0);
        Warrior unitC = createDummyUnit(posC);
        ((Floor) board.getCell(0, 0)).setOccupant(unitC);

        // Test 1: Range 0.0 (Should only find the unit on the exact tile)
        List<Unit> rangeZero = board.getUnitsInRange(posA, 0.0);
        assertEquals(1, rangeZero.size(), "Should only find Unit A at distance 0.");
        assertTrue(rangeZero.contains(unitA));

        // Test 2: Range 1.0 (Should find A and B, but not C because 1.414 > 1.0)
        List<Unit> rangeOne = board.getUnitsInRange(posA, 1.0);
        assertEquals(2, rangeOne.size(), "Should find Unit A and Unit B.");
        assertTrue(rangeOne.contains(unitA));
        assertTrue(rangeOne.contains(unitB));
        assertFalse(rangeOne.contains(unitC), "Unit C is too far (1.414 > 1.0).");

        // Test 3: Range 1.5 (Should find all three units)
        List<Unit> rangeOnePointFive = board.getUnitsInRange(posA, 1.5);
        assertEquals(3, rangeOnePointFive.size(), "Should find all units within 1.5 range.");
        assertTrue(rangeOnePointFive.contains(unitC));
    }

    @Test
    void testGetUnitsInRange_EdgeOfBoardBounds() {
        Position cornerPos = new Position(4, 4);
        Warrior cornerUnit = createDummyUnit(cornerPos);
        Floor cornerFloor = new Floor(cornerPos, cornerUnit);
        board.setCell(4, 4, cornerFloor);

        // Search from (4, 4) with a large range. The method should not throw IndexOutOfBoundsException.
        List<Unit> foundUnits = board.getUnitsInRange(cornerPos, 10.0);

        assertEquals(1, foundUnits.size(), "Should find the corner unit.");
        assertTrue(foundUnits.contains(cornerUnit));
    }

    @Test
    void testToString_FormatsGridCorrectly() {
        // Fill a 2x2 board
        GameBoard smallBoard = new GameBoard(2, 2);

        smallBoard.setCell(0, 0, new Floor(new Position(0, 0), null));
        smallBoard.setCell(1, 0, new Floor(new Position(1, 0), null));

        // Add a unit to one of the cells to test toString delegation
        Warrior p = createDummyUnit(new Position(0, 1));
        smallBoard.setCell(0, 1, new Floor(new Position(0, 1), p));
        smallBoard.setCell(1, 1, new Floor(new Position(1, 1), null));

        String expectedOutput = "..\n@.\n"; // Assuming floor without unit is '.' and warrior is '@'

        assertEquals(expectedOutput, smallBoard.toString(), "Board string representation should match the grid cells.");
    }
}