package tests.unitsTest;

import Game.Floor;
import Game.Position;
import Units.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

    private Unit createDummyUnit(String name, String tileString, boolean advanceAcceptResult) {
        return new Unit(name, 100, 100, 10, 5, new Position(0, 0), tileString, null, msg -> {}) {
            @Override
            public boolean AttackAccept(Unit unit) { return false; }
            @Override
            public boolean AdvanceAccept(Unit unit) { return advanceAcceptResult; }
            @Override
            public int GetRange() { return 1; }
            @Override
            public void GameTick() {}
        };
    }

    @Test
    void testConstructor_SetsTileToDotAndStoresOccupant() {
        Unit occupant = createDummyUnit("Occupant", "O", true);
        Floor floor = new Floor(new Position(1, 1), occupant);

        assertEquals(".", floor.getTile(), "Floor's constructor should always set the tile to \".\", regardless of the occupant.");
        assertEquals(occupant, floor.getOccupant(), "Constructor should store the given unit as the occupant.");
    }

    @Test
    void testConstructor_NullOccupant_StartsEmpty() {
        Floor floor = new Floor(new Position(1, 1), null);

        assertNull(floor.getOccupant(), "Floor should start with no occupant when constructed with null.");
    }

    @Test
    void testGetOccupant_ReturnsCurrentOccupant() {
        Unit occupant = createDummyUnit("Occupant", "O", true);
        Floor floor = new Floor(new Position(0, 0), occupant);

        assertEquals(occupant, floor.getOccupant());
    }

    @Test
    void testSetOccupant_UpdatesOccupant() {
        Floor floor = new Floor(new Position(0, 0), null);
        Unit newOccupant = createDummyUnit("NewOccupant", "N", true);

        floor.setOccupant(newOccupant);

        assertEquals(newOccupant, floor.getOccupant(), "setOccupant should update the occupant field directly.");
    }

    @Test
    void testAccept_EmptyFloor_AlwaysAcceptsAndSetsOccupant() {
        Floor floor = new Floor(new Position(0, 0), null);
        Unit entering = createDummyUnit("Entering", "E", false);

        boolean result = floor.Accept(entering);

        assertTrue(result, "Accept should return true when the floor is empty, regardless of AdvanceAccept.");
        assertEquals(entering, floor.getOccupant(), "The entering unit should become the new occupant.");
    }

    @Test
    void testAccept_OccupiedFloor_ExistingOccupantAcceptsAdvance_EntersAndReplacesOccupant() {
        Unit existing = createDummyUnit("Existing", "X", true);
        Floor floor = new Floor(new Position(0, 0), existing);
        Unit entering = createDummyUnit("Entering", "E", false);

        boolean result = floor.Accept(entering);

        assertTrue(result, "Accept should return true when the existing occupant's AdvanceAccept allows the advance.");
        assertEquals(entering, floor.getOccupant(), "The entering unit should replace the previous occupant.");
    }

    @Test
    void testAccept_OccupiedFloor_ExistingOccupantRejectsAdvance_StaysOccupiedByOriginal() {
        Unit existing = createDummyUnit("Existing", "X", false);
        Floor floor = new Floor(new Position(0, 0), existing);
        Unit entering = createDummyUnit("Entering", "E", false);

        boolean result = floor.Accept(entering);

        assertFalse(result, "Accept should return false when the existing occupant's AdvanceAccept rejects the advance.");
        assertEquals(existing, floor.getOccupant(), "The original occupant should remain unchanged when the advance is rejected.");
    }

    @Test
    void testToString_EmptyFloor_ReturnsDotTile() {
        Floor floor = new Floor(new Position(0, 0), null);

        assertEquals(".", floor.toString(), "toString should return the tile (\".\") when there is no occupant.");
    }

    @Test
    void testToString_OccupiedFloor_ReturnsOccupantToString() {
        Unit occupant = createDummyUnit("Occupant", "O", true);
        Floor floor = new Floor(new Position(0, 0), occupant);

        assertEquals("O", floor.toString(), "toString should delegate to the occupant's toString when one is present.");
    }

    @Test
    void testToString_AfterOccupantLeaves_ReturnsDotAgain() {
        Unit occupant = createDummyUnit("Occupant", "O", true);
        Floor floor = new Floor(new Position(0, 0), occupant);

        floor.setOccupant(null);

        assertEquals(".", floor.toString(), "toString should fall back to the tile once the occupant is cleared.");
    }

    @Test
    void testGetPos_ReturnsConstructorValue() {
        Floor floor = new Floor(new Position(4, 6), null);

        assertEquals(new Position(4, 6), floor.getPos(), "getPos should return the position given to the constructor.");
    }
}