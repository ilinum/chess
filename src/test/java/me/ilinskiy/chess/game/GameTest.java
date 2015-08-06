package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Board;
import me.ilinskiy.chess.chessBoard.Coordinates;
import me.ilinskiy.chess.chessBoard.PieceColor;
import org.junit.Test;

import static me.ilinskiy.chess.chessBoard.Board.BOARD_SIZE;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/31/15.
 */
public class GameTest {

    @Test
    public void testGameInit() {
        Player p1 = new PlayerMock(PieceColor.White);
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new Game(p1, p2, null);
        assertTrue(!g.isGameOver());
        assertTrue(!g.getWinner().isPresent());
        assertTrue(g.getMovesMade().isEmpty());
        assertTrue(g.numberOfMovesMade() == 0);
        assertTrue(g.whoseTurnIsIt() == p1.getPlayerColor());
    }

    @Test(timeout = 1000)
    public void testMakeMove() {
        Move move = new Move(new Coordinates(1, BOARD_SIZE - 1), new Coordinates(2, BOARD_SIZE - 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b) {
                return move;
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new Game(p1, p2, null);
        g.makeMove();
        assertTrue(!g.isGameOver());
        assertTrue(!g.getWinner().isPresent());
        assertTrue(g.getMovesMade().size() == 1);
        assertTrue(g.getMovesMade().get(0).equals(move));
        assertTrue(g.numberOfMovesMade() == 1);
        assertTrue(g.whoseTurnIsIt() == p2.getPlayerColor());
    }

    @Test(expected = RuntimeException.class)
    public void testIllegalMove() {
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b) {
                return new Move(new Coordinates(8, 8), new Coordinates(1, 1));
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new Game(p1, p2, null);
        g.makeMove();
    }
}
