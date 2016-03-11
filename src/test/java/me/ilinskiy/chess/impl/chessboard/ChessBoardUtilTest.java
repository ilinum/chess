package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.Move;
import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.Test;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class ChessBoardUtilTest {

    @Test
    public void testMoveAndEvaluate() {
        Board b = new BoardImpl();
        assertTrue(b.whoseTurnIsIt() == PieceColor.White);
        Board copy = b.copy();
        Move m = new RegularMove(new CoordinatesImpl(BOARD_SIZE - 1, BOARD_SIZE - 1), new CoordinatesImpl(1, 1));
        ChessBoardUtil.makeMoveAndEvaluate(b, m, b1 -> false);
        assertTrue(b.whoseTurnIsIt() == PieceColor.White);
        assertTrue(copy.equals(b));
    }

    @Test
    public void testInverseColor() {
        assertTrue(PieceColor.Black.inverse() == PieceColor.White);
        assertTrue(PieceColor.White.inverse() == PieceColor.Black);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseColor2() {
        PieceColor.Empty.inverse();
    }
}
