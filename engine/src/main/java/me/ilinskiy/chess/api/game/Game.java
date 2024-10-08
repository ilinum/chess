package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface Game {
    void makeMove(@NotNull Move m);

    @NotNull
    PieceColor whoseTurnIsIt();

    boolean isGameOver();

    @NotNull
    Optional<PieceColor> getWinner();

    @NotNull
    List<Move> getMovesMade();

    int numberOfMovesMade();

    @NotNull
    MoveAwareBoard getBoard();
}
