package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface Game {
    void makeMove();

    @NotNull
    PieceColor whoseTurnIsIt();

    boolean isGameOver();

    @NotNull
    Optional<PieceColor> getWinner();

    @NotNull
    List<Move> getMovesMade();

    int numberOfMovesMade();
}
