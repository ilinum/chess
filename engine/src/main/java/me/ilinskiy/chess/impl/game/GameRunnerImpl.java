package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.ChessPainter;
import me.ilinskiy.chess.api.ui.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

import static me.ilinskiy.chess.impl.game.GameUtil.println;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunnerImpl implements GameRunner {
    @Nullable
    private final ChessPainter painter;
    static final boolean DEBUG = false;
    public static int TIMEOUT_IN_SECONDS = 0; //if timeout is 0, the only limit is yourself

    public GameRunnerImpl(@Nullable ChessPainter p) {
        painter = p;
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
        Game g = new GameImpl(p1.getPlayerColor(), painter);
        PieceColor winner = null;
        Player current = p1;
        while (winner == null) {
            try {
                Move move = getMove(current, g.getBoard());
                if (move == null) {
                    //it timed out
                    winner = g.whoseTurnIsIt().inverse();
                    println("Timed out!");
                } else {
                    assert current.getPlayerColor() == g.whoseTurnIsIt();
                    g.makeMove(move, current::getPieceTypeForPromotedPawn);
                    if (g.isGameOver()) {
                        winner = g.getWinner().orElse(null);
                        assert winner != null;
                    } else {
                        current = current.inverse(p1, p2);
                        assert current.getPlayerColor() == g.whoseTurnIsIt();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (painter != null) {
            painter.gameOver(winner);
        }
        return winner;
    }

    private Move getMove(@NotNull Player p, @NotNull Board b) {
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
            if (painter != null) {
                painter.moveStarted();
            }
            if (GameRunnerImpl.TIMEOUT_IN_SECONDS > 0) {
                result = future.get(GameRunnerImpl.TIMEOUT_IN_SECONDS + 1, TimeUnit.SECONDS); //be nice and add an extra second
            } else {
                result = future.get();
            }
        } catch (InterruptedException | ExecutionException ignored) {

        } catch (TimeoutException e) {
            future.cancel(true);
        } finally {
            if (painter != null) {
                painter.moveFinished();
            }
        }
        return result;
    }


    /**
     * creates a dialog that asks if user wants to play again
     *
     * @return true if play again
     */
    @Override
    public boolean askToPlayAgain() {
        return painter != null && painter.askToPlayAgain();
    }

    /**
     * Ask the user the timeout they want to use. It sets the timeout according to the answer
     */
    @Override
    public void askTimeOut() {
        if (painter != null) {
            TIMEOUT_IN_SECONDS = painter.askTimeOut();
        }
        if (TIMEOUT_IN_SECONDS < 0) {
            throw new RuntimeException("Timeout cannot be negative!");
        }
    }

    /**
     * Disposes of the frame
     */
    @Override
    public void dispose() {
        if (painter != null) {
            painter.dispose();
        }
    }
}
