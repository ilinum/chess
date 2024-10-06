package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.jupiter.api.Test;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class ChessBoardUtilTest {

    @Test
    public void testMoveAndEvaluate() {
        Board b = new BoardImpl();
        Board copy = b.copy();
        Move m = new RegularMove(new CoordinatesImpl(BOARD_SIZE - 1, BOARD_SIZE - 1), new CoordinatesImpl(1, 1));
        ChessBoardUtil.makeMoveAndEvaluate(b, m, b1 -> false);
        assertEquals(copy, b);
    }

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
