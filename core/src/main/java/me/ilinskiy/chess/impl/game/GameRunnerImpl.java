package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;
import java.util.function.Function;


/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunnerImpl implements GameRunner {
    private static int timeoutInSeconds = 0; //if timeout is 0, the only limit is yourself

    @Nullable
    private Function<MoveAwareBoard, Void> moveMadeCallback;

    public GameRunnerImpl(int timeoutSeconds) {
        this(timeoutSeconds, null);
    }

    public GameRunnerImpl(int timeoutSeconds, @Nullable Function<MoveAwareBoard, Void> moveMadeCallback) {
        timeoutInSeconds = timeoutSeconds;
        this.moveMadeCallback = moveMadeCallback;
    }

    /**
     * Run the actual game with the given players
     * ChessBoardUtil.inverse(p1.getPlayerColor()) == p2.getPlayerColor() must be true
     * At the end of the game shows the dialog, who won
     *
     * @param p1 player 1
     * @param p2 player 2
     */
    @Override
    @NotNull
    public PieceColor runGame(@NotNull Player p1, @NotNull Player p2) {
        Player current = p1.getColor() == PieceColor.White ? p1 : p2;
        Game g = new GameImpl(current.getColor());
        PieceColor winner = null;
        while (winner == null) {
            try {
                Move move = getMove(current, g.getBoard());
                if (move == null) {
                    //it timed out
                    winner = g.getTurn().inverse();
                } else {
                    assert current.getColor() == g.getTurn();
                    g.makeMove(move);
                    if (moveMadeCallback != null) {
                        moveMadeCallback.apply(g.getBoard());
                    }
                    if (g.isGameOver()) {
                        winner = g.getWinner().orElse(null);
                        assert winner != null;
                    } else {
                        current = current.inverse(p1, p2);
                        assert current.getColor() == g.getTurn();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return winner;
    }

    private Move getMove(@NotNull Player p, @NotNull MoveAwareBoard b) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Move> future = executorService.submit(() -> {
            Move m = null;
            while (m == null) {
                m = p.getMove(b);
            }
            return m;
        });
        Move result = null;
        try {
            if (GameRunnerImpl.timeoutInSeconds > 0) {
                result = future.get(GameRunnerImpl.timeoutInSeconds + 1,
                                    TimeUnit.SECONDS); //be nice and add an extra second
            } else {
                result = future.get();
            }
        } catch (InterruptedException | ExecutionException ignored) {

        } catch (TimeoutException e) {
            future.cancel(true);
        }
        return result;
    }
}
