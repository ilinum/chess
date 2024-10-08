package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PieceColorTest {

    @Test
    public void testInverseColor() {
        assertSame(PieceColor.Black.inverse(), PieceColor.White);
        assertSame(PieceColor.White.inverse(), PieceColor.Black);
    }

    @Test
    public void testInverseColor2() {
        assertThrows(IllegalArgumentException.class, PieceColor.Empty::inverse);
    }
}
