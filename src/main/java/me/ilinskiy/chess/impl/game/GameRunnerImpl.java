package me.ilinskiy.chess.impl.game;

import org.jetbrains.annotations.NotNull;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.ui.ChessPainter;
import me.ilinskiy.chess.api.ui.Player;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunnerImpl implements GameRunner {
    private final ChessPainter painter;
    public static final boolean DEBUG = false;
    public static int TIMEOUT_IN_SECONDS = 0; //if timeout is 0, the only limit is yourself

    public GameRunnerImpl(ChessPainter p) {
        painter = p;
    }

    /**
     * Run the actual game with the given players
     * ChessBoardUtil.inverse(p1.getPlayerColor()) == p2.getPlayerColor() must be true
     * At the end of the game shows the dialog, who won
     *
     * @param p1 player 1
     * @param p2 player 2
     * @return the piece color of the winner or PieceColor.Empty if it's a draw
     */
    @Override
    @NotNull
    public PieceColor runGame(@org.jetbrains.annotations.NotNull Player p1, @org.jetbrains.annotations.NotNull Player p2) {
        Game g;
        g = new GameImpl(p1, p2, painter);
        while (!g.isGameOver()) {
            try {
                g.makeMove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PieceColor winner = g.getWinner().get();
        painter.showWinner(winner);
        return winner;
    }

    /**
     * creates a dialog that asks if user wants to play again
     *
     * @return true if play again
     */
    @Override
    public boolean askToPlayAgain() {
        return painter.askToPlayAgain();
    }

    /**
     * Ask the user the timeout they want to use. It sets the timeout according to the answer
     */
    @Override
    public void askTimeOut() {
        TIMEOUT_IN_SECONDS = painter.askTimeOut();
        if (TIMEOUT_IN_SECONDS < 0) {
            throw new RuntimeException("Timeout cannot be negative!");
        }
    }

    /**
     * Disposes of the frame
     */
    @Override
    public void dispose() {
        painter.dispose();
    }
}
