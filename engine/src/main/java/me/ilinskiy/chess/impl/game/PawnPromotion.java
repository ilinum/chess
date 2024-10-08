package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class PawnPromotion extends MoveAdapter {
    public static final PieceType[] PROMOTION_ALLOWED_TO = {
            PieceType.Queen, PieceType.Rook, PieceType.Bishop, PieceType.Knight
    };

    @NotNull
    public final Coordinates initialPosition;
    @NotNull
    public final Coordinates newPosition;
    @NotNull
    public final PieceType promoteTo;

    public PawnPromotion(
            @NotNull Coordinates initialPosition,
            @NotNull Coordinates newPosition,
            @NotNull PieceType promoteTo) {
        if (!Arrays.asList(PROMOTION_ALLOWED_TO).contains(promoteTo)) {
            throw new IllegalArgumentException("Promotion not allowed to " + promoteTo);
        }
        this.initialPosition = initialPosition;
        this.newPosition = newPosition;
        this.promoteTo = promoteTo;
    }

    @Override
    public Coordinates[] getInitialPositions() {
        return new Coordinates[]{initialPosition};
    }

    @Override
    public Coordinates[] getNewPositions() {
        return new Coordinates[]{newPosition};
    }

    @Override
    public Move copy() {
        return new PawnPromotion(initialPosition, newPosition, promoteTo);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PawnPromotion pp) {
            return this.newPosition.equals(pp.newPosition) && this.initialPosition.equals(pp.initialPosition) && this.promoteTo == pp.promoteTo;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(promoteTo, initialPosition, newPosition);
    }

    @Override
    public int compareTo(@NotNull Move move) {
        int cmp = super.compareTo(move);
        if (cmp != 0) {
            return cmp;
        }
        if (move instanceof PawnPromotion pp) {
            return this.promoteTo.compareTo(pp.promoteTo);
        }
        return 1;
    }
}
