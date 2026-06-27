package tests.gameTests;

import Game.Position;
import Game.Wall;
import Units.Unit;
import Units.Warrior;
import combat.CombatSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {
    private Wall wall;
    private Position position;
    private Unit dummyUnit;

    @BeforeEach
    void setUp() {
        position = new Position(5, 5);
        wall = new Wall(position);

        CombatSystem dummyCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) { return 0; }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) { return false; }
        };
        dummyUnit = new Warrior("Dummy", 100, 10, 10, new Position(0, 0), dummyCombatSystem, "@", 3, msg -> {});
    }

    @AfterEach
    void tearDown() {
        wall = null;
        position = null;
        dummyUnit = null;
    }

    @Test
    void testConstructorAndProperties() {
        assertEquals("#", wall.getTile(), "Wall tile should be '#'.");
        assertEquals("#", wall.toString(), "Wall toString should return '#'.");
        assertEquals(position, wall.getPos(), "Wall position should match the one passed in constructor.");
    }

    @Test
    void testAccept_AlwaysReturnsFalse() {
        assertFalse(wall.Accept(dummyUnit), "Wall should never accept a unit.");
        assertFalse(wall.Accept(null), "Wall should return false even for null unit.");
    }

    @Test
    void testGetOccupant_AlwaysReturnsNull() {
        assertNull(wall.getOccupant(), "Wall should not have an occupant.");
    }
}