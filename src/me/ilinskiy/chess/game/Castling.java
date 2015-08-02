package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/2/15.
 */
public class Castling extends Move {
    private final Coordinates rookNewPosition;
    private final Coordinates rookInitPosition;


    public Castling(@NotNull Coordinates kingInitPos, @NotNull Coordinates kingNewPos, @NotNull Coordinates rookInitPos,
                    @NotNull Coordinates rookNewPos) {
        super(kingInitPos, kingNewPos);
        rookInitPosition = rookInitPos;
        rookNewPosition = rookNewPos;
    }

    public Coordinates getKingInitialPosition() {
        return getInitialPosition();
    }

    public Coordinates getKingNewPosition() {
        return getNewPosition();
    }

    public Coordinates getRookInitialPosition() {
        return rookInitPosition;
    }

    public Coordinates getRookNewPosition() {
        return rookNewPosition;
    }

    @Override
    public Castling copy() {
        return new Castling(getKingInitialPosition(), getKingNewPosition(), getRookInitialPosition(), getRookNewPosition());
    }
}
