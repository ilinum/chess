package me.ilinskiy.chess.api.chessboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinatesTest {
    @Test
    public void testUCINotation() {
        assertEquals("a1", new Coordinates(0, Board.BOARD_SIZE-1).toUCINotation());
        assertEquals("d7", new Coordinates(3, 1).toUCINotation());
        assertEquals(new Coordinates(0, Board.BOARD_SIZE-1), Coordinates.fromUCINotation("a1"));
        assertEquals(new Coordinates(3, 1), Coordinates.fromUCINotation("d7"));
    }
}
