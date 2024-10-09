package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.game.Castling;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTest {
    @Test
    public void getUCIStringTest() {
        Move m = new RegularMove(new Coordinates(4, 6), new Coordinates(4, 4));
        assertEquals("e2e4", m.getUCIString());
        m = new Castling(
                new Coordinates(4, 7), new Coordinates(6, 7),
                new Coordinates(7, 7), new Coordinates(5, 7));
        assertEquals("e1g1", m.getUCIString());
        m = new PawnPromotion(new Coordinates(4, 1), new Coordinates(4, 0), PieceType.Queen);
        assertEquals("e7e8q", m.getUCIString());
    }
}
