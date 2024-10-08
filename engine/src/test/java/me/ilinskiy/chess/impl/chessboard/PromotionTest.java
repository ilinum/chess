package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionTest {
    @Test
    public void testCreatePromotion() {
        Coordinates c1 = new Coordinates(1, 1);
        Coordinates c2 = new Coordinates(1, 0);
        PawnPromotion pp = new PawnPromotion(c1, c2, PieceType.Queen);
        pp = new PawnPromotion(c1, c2, PieceType.Rook);
        pp = new PawnPromotion(c1, c2, PieceType.Bishop);
        pp = new PawnPromotion(c1, c2, PieceType.Knight);
        assertThrows(IllegalArgumentException.class, () -> {
           new PawnPromotion(c1, c2, PieceType.Pawn);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new PawnPromotion(c1, c2, PieceType.King);
        });
    }

    @Test
    public void testPromotionEquals() {
        Coordinates c1 = new Coordinates(1, 1);
        Coordinates c2 = new Coordinates(1, 0);
        PawnPromotion pp = new PawnPromotion(c1, c2, PieceType.Bishop);
        PawnPromotion pp2 = new PawnPromotion(c1, c2, PieceType.Bishop);
        assertEquals(pp, pp2);
        assertEquals(pp, pp.copy());
        PawnPromotion queenPromo = new PawnPromotion(c1, c2, PieceType.Queen);
        assertNotEquals(queenPromo, pp);
    }
}
