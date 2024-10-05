package me.ilinskiy.chess.game;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.game.GameImpl;
import me.ilinskiy.chess.impl.game.PlayerMock;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;

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
        Game g = new GameImpl(p1.getPlayerColor());
        assertFalse(g.isGameOver());
        assertFalse(g.getWinner().isPresent());
        assertTrue(g.getMovesMade().isEmpty());
        assertEquals(0, g.numberOfMovesMade());
        assertSame(g.whoseTurnIsIt(), p1.getPlayerColor());
    }

    @Test
    @Timeout(value = 1)
    public void testMakeMove() {
        Move move = new RegularMove(new CoordinatesImpl(1, BOARD_SIZE - 1), new CoordinatesImpl(2, BOARD_SIZE - 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b, @NotNull List<Move> availableMoves) {
                return move;
            }
        };
        Player p2 = new PlayerMock(PieceColor.Black);
        Game g = new GameImpl(p1.getPlayerColor());
        g.makeMove(p1.getMove(g.getBoard(), new ArrayList<>()), p1::getPieceTypeForPromotedPawn);
        assertFalse(g.isGameOver());
        assertFalse(g.getWinner().isPresent());
        assertEquals(1, g.getMovesMade().size());
        assertEquals(g.getMovesMade().get(0), move);
        assertEquals(1, g.numberOfMovesMade());
        assertSame(g.whoseTurnIsIt(), p2.getPlayerColor());
    }

    @Test
    @Timeout(value = 1)
    public void testMakeMoveWrongPlayer() {
        Move move = new RegularMove(new CoordinatesImpl(1, 1), new CoordinatesImpl(1, 3));
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b, @NotNull List<Move> availableMoves) {
                return move;
            }
        };
        Exception exc = assertThrows(RuntimeException.class, () -> {
            // Moving wrong player.
            Game g = new GameImpl(p1.getPlayerColor());
            g.makeMove(p1.getMove(g.getBoard(), new ArrayList<>()), p1::getPieceTypeForPromotedPawn);
        });
        assertEquals(exc.getMessage(), "You can only move pieces of your color!");
    }

    @Test
    public void testIllegalMove() {
        Player p1 = new PlayerMock(PieceColor.White) {
            @NotNull
            @Override
            public Move getMove(@NotNull Board b, @NotNull List<Move> availableMoves) {
                return new RegularMove(new CoordinatesImpl(8, 8), new CoordinatesImpl(1, 1));
            }
        };
        Game g = new GameImpl(p1.getPlayerColor());
        assertThrows(RuntimeException.class, () -> {
            g.makeMove(p1.getMove(g.getBoard(), new ArrayList<>()), p1::getPieceTypeForPromotedPawn);
        });

    }
}
