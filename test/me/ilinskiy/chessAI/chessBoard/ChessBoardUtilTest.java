package me.ilinskiy.chessAI.chessBoard;

import me.ilinskiy.chessAI.game.Move;
import org.junit.Test;

import static me.ilinskiy.chessAI.chessBoard.ImmutableBoard.BOARD_SIZE;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class ChessBoardUtilTest {

    @Test
    public void testMoveAndEvalute() {
        ImmutableBoard b = new ImmutableBoard();
        assertTrue(b.whoseTurnIsIt() == PieceColor.White);
        ImmutableBoard copy = b.copy();
        Move m = new Move(new Coordinates(BOARD_SIZE - 1, BOARD_SIZE - 1), new Coordinates(1, 1));
        ChessBoardUtil.makeMoveAndEvaluate(b, m, b1 -> false);
        assertTrue(b.whoseTurnIsIt() == PieceColor.White);
        assertTrue(copy.equals(b));
    }

    @Test
    public void testInverseColor() {
        assertTrue(ChessBoardUtil.inverse(PieceColor.Black) == PieceColor.White);
        assertTrue(ChessBoardUtil.inverse(PieceColor.White) == PieceColor.Black);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseColor2() {
        ChessBoardUtil.inverse(PieceColor.Empty);
    }
}
