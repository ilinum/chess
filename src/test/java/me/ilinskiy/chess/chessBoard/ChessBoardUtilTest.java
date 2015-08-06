package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.game.Move;
import org.junit.Test;

import static me.ilinskiy.chess.chessBoard.Board.BOARD_SIZE;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class ChessBoardUtilTest {

    @Test
    public void testMoveAndEvaluate() {
        Board b = new Board();
        assertTrue(b.whoseTurnIsIt() == PieceColor.White);
        Board copy = b.copy();
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
