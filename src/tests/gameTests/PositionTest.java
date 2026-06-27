package tests.gameTests;

import Game.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testConstructorAndGetters() {
        Position p = new Position(5, -3);

        assertEquals(5, p.getX());
        assertEquals(-3, p.getY());
    }

    @Test
    void testRange() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);
        Position p3 = new Position(-3, -4);
        Position p4 = new Position(0, 0);

        assertEquals(5.0, p1.range(p2), 0.0001);
        assertEquals(5.0, p1.range(p3), 0.0001);
        assertEquals(10.0, p2.range(p3), 0.0001);
        assertEquals(0.0, p1.range(p4), 0.0001);
    }

    @Test
    void testEquals() {
        Position p1 = new Position(2, 2);
        Position p2 = new Position(2, 2);
        Position p3 = new Position(3, 2);
        Position p4 = new Position(2, 3);

        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
        assertNotEquals(p1, null);
        assertNotEquals(p1, "(2, 2)");
    }

    @Test
    void testHashCode() {
        Position p1 = new Position(10, 20);
        Position p2 = new Position(10, 20);
        Position p3 = new Position(20, 10);

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void testToString() {
        Position p1 = new Position(8, 15);
        Position p2 = new Position(-4, 0);

        assertEquals("(8, 15)", p1.toString());
        assertEquals("(-4, 0)", p2.toString());
    }
}