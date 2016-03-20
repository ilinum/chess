package me.ilinskiy.chess.game;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.game.GameImpl;
import me.ilinskiy.chess.impl.game.PlayerMock;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;
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
        Game g = new GameImpl(p1, p2, null);
        assertTrue(!g.isGameOver());
        assertTrue(!g.getWinner().isPresent());
        assertTrue(g.getMovesMade().isEmpty());
        assertTrue(g.numberOfMovesMade() == 0);
        assertTrue(g.whoseTurnIsIt() == p1.getPlayerColor());
    }

    @Test(timeout = 1000)
    public void testMakeMove() {
        Move move = new RegularMove(new CoordinatesImpl(1, BOARD_SIZE - 1), new CoordinatesImpl(2, BOARD_SIZE - 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b) {
                return move;
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new GameImpl(p1, p2, null);
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
                return new RegularMove(new CoordinatesImpl(8, 8), new CoordinatesImpl(1, 1));
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new GameImpl(p1, p2, null);
        g.makeMove();
    }
}
