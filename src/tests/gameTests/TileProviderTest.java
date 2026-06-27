package tests.gameTests;

import Game.Position;
import Game.TileProvider;
import Units.Boss;
import Units.Enemy;
import Units.Monster;
import Units.Trap;
import Units.Unit;
import combat.CombatSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileProviderTest {

    private CombatSystem dummyCombatSystem;
    private Position dummyPosition;

    @BeforeEach
    void setUp() {
        dummyPosition = new Position(3, 4);
        dummyCombatSystem = new CombatSystem() {
            @Override
            public int Combat(Unit attacker, Unit defender) { return 0; }
            @Override
            public boolean Attack(Unit otherUnit, double damage, String attackerName) { return false; }
        };
    }

    @AfterEach
    void tearDown() {
        dummyPosition = null;
        dummyCombatSystem = null;
    }

    @Test
    void testCreateEnemy_Monsters() {
        Enemy goldCloak = TileProvider.createEnemy('s', dummyPosition, dummyCombatSystem, msg -> {});
        assertNotNull(goldCloak);
        assertTrue(goldCloak instanceof Monster);
        assertEquals("Gold Cloak", goldCloak.getName());
        assertEquals("s", goldCloak.toString());
        assertEquals(dummyPosition, goldCloak.getPos());

        Enemy whiteWalker = TileProvider.createEnemy('w', dummyPosition, dummyCombatSystem, msg -> {});
        assertNotNull(whiteWalker);
        assertTrue(whiteWalker instanceof Monster);
        assertEquals("White Walker", whiteWalker.getName());
        assertEquals("w", whiteWalker.toString());
    }

    @Test
    void testCreateEnemy_Bosses() {
        Enemy mountain = TileProvider.createEnemy('M', dummyPosition, dummyCombatSystem, msg -> {});
        assertNotNull(mountain);
        assertTrue(mountain instanceof Boss);
        assertEquals("The Mountain", mountain.getName());
        assertEquals("M", mountain.toString());

        Enemy nightsKing = TileProvider.createEnemy('K', dummyPosition, dummyCombatSystem, msg -> {});
        assertNotNull(nightsKing);
        assertTrue(nightsKing instanceof Boss);
        assertEquals("Night's King", nightsKing.getName());
        assertEquals("K", nightsKing.toString());
    }

    @Test
    void testCreateEnemy_Traps() {
        Enemy deathTrap = TileProvider.createEnemy('D', dummyPosition, dummyCombatSystem, msg -> {});
        assertNotNull(deathTrap);
        assertTrue(deathTrap instanceof Trap);
        assertEquals("Death Trap", deathTrap.getName());
        assertEquals("D", deathTrap.toString());
    }

    @Test
    void testCreateEnemy_InvalidCharacters_ReturnsNull() {
        assertNull(TileProvider.createEnemy('a', dummyPosition, dummyCombatSystem, msg -> {}));
        assertNull(TileProvider.createEnemy('@', dummyPosition, dummyCombatSystem, msg -> {}));
        assertNull(TileProvider.createEnemy('.', dummyPosition, dummyCombatSystem, msg -> {}));
        assertNull(TileProvider.createEnemy('#', dummyPosition, dummyCombatSystem, msg -> {}));
        assertNull(TileProvider.createEnemy('1', dummyPosition, dummyCombatSystem, msg -> {}));
    }
}