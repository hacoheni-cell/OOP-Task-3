package tests.unitsTest;

import Game.Cell;
import Game.Position;
import Units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell testCell;

    @BeforeEach
    void setUp() {
        testCell = new Cell(new Position(2, 3), "F") {
            @Override
            public boolean Accept(Unit unit) {
                return true;
            }
        };
    }

    @Test
    void testConstructor_SetsPositionAndTile() {
        assertEquals(new Position(2, 3), testCell.getPos(), "Constructor should store the given position.");
        assertEquals("F", testCell.getTile(), "Constructor should store the given tile string.");
    }

    @Test
    void testGetTile_ReturnsConstructorValue() {
        assertEquals("F", testCell.getTile(), "getTile should return the tile field as-is.");
    }

    @Test
    void testGetPos_ReturnsConstructorValue() {
        assertEquals(new Position(2, 3), testCell.getPos(), "getPos should return the position field as-is.");
    }

    @Test
    void testSetPos_UpdatesPosition() {
        testCell.setPos(new Position(5, 7));
        assertEquals(new Position(5, 7), testCell.getPos(), "setPos should update the position field.");
    }

    @Test
    void testGetOccupant_DefaultsToNull() {
        assertNull(testCell.getOccupant(), "Cell's default getOccupant implementation should return null unless overridden.");
    }

    @Test
    void testToString_ReturnsTile() {
        assertEquals("F", testCell.toString(), "toString should return the tile field.");
    }

    @Test
    void testToString_ReflectsTileChangeThroughSetPos() {
        // Sanity check that toString is driven by the tile field, independent of position.
        testCell.setPos(new Position(9, 9));
        assertEquals("F", testCell.toString(), "toString should remain based on tile, unaffected by position changes.");
    }

    @Test
    void testAccept_IsAbstractAndDelegatesToSubclassImplementation() {
        Cell rejectingCell = new Cell(new Position(0, 0), "X") {
            @Override
            public boolean Accept(Unit unit) {
                return false;
            }
        };

        assertTrue(testCell.Accept(null), "The accepting subclass implementation should return true.");
        assertFalse(rejectingCell.Accept(null), "The rejecting subclass implementation should return false.");
    }
}