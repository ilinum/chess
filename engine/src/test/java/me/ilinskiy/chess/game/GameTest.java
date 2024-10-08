package me.ilinskiy.chess.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.game.GameImpl;
import me.ilinskiy.chess.impl.game.PlayerMock;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/31/15.
 */
public class GameTest {

    @Test
    public void testGameInit() {
        Player p1 = new PlayerMock(PieceColor.White);
        Game g = new GameImpl(p1.getColor());
        assertFalse(g.isGameOver());
        assertFalse(g.getWinner().isPresent());
        assertTrue(g.getBoard().getMoves().isEmpty());
        assertSame(g.getTurn(), p1.getColor());
    }

    @Test
    @Timeout(value = 1)
    public void testMakeMove() {
        Move move = new RegularMove(new Coordinates(1, BOARD_SIZE - 1), new Coordinates(2, BOARD_SIZE - 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull MoveAwareBoard b) {
                return move;
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new GameImpl(p1.getColor());
        g.makeMove(p1.getMove(g.getBoard()));
        assertFalse(g.isGameOver());
        assertFalse(g.getWinner().isPresent());
        assertEquals(1, g.getBoard().getMoves().size());
        assertEquals(g.getBoard().getMoves().getFirst(), move);
        assertEquals(1, g.getBoard().getMoves().size());
        assertSame(g.getTurn(), p2.getColor());
    }

    @Test
    @Timeout(value = 1)
    public void testMakeMoveWrongPlayer() {
        Move move = new RegularMove(new Coordinates(1, 1), new Coordinates(1, 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull MoveAwareBoard b) {
                return move;
            }
        };
        Exception exc = assertThrows(RuntimeException.class, () -> {
            // Moving wrong player.
            Game g = new GameImpl(p1.getColor());
            g.makeMove(p1.getMove(g.getBoard()));
        });
        assertEquals(exc.getMessage(), "You can only move pieces of your color!");
    }

    @Test
    public void testIllegalMove() {
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull MoveAwareBoard b) {
                return new RegularMove(new Coordinates(8, 8), new Coordinates(1, 1));
            }
        };
        Game g = new GameImpl(p1.getColor());
        assertThrows(RuntimeException.class, () -> {
            g.makeMove(p1.getMove(g.getBoard()));
        });

    }
}
