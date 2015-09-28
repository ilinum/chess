package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.ui.ChessPainter;
import me.ilinskiy.chess.ui.JSwingChessPainter;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunner {
    private static final ChessPainter painter = new JSwingChessPainter();
    public static final boolean DEBUG = false;
    public static int TIMEOUT_IN_SECONDS = 0; //if timeout is 0, the only limit is yourself


    /**
     * Run the actual game with the given players
     * ChessBoardUtil.inverse(p1.getPlayerColor()) == p2.getPlayerColor() must be true
     * At the end of the game shows the dialog, who won
     *
     * @param p1 player 1
     * @param p2 player 2
     * @return the piece color of the winner or PieceColor.Empty if it's a draw
     */
    @NotNull
    public static PieceColor runGame(@NotNull Player p1, @NotNull Player p2) {
        Game g;
        g = new Game(p1, p2, painter);
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
    public static boolean askToPlayAgain() {
        return painter.askToPlayAgain();
    }

    /**
     * Ask the user the timeout they want to use. It sets the timeout according to the answer
     */
    public static void askTimeOut() {
        TIMEOUT_IN_SECONDS = painter.askTimeOut();
        if (TIMEOUT_IN_SECONDS < 0) {
            throw new RuntimeException("Timeout cannot be negative!");
        }
    }

    /**
     * Disposes of the frame
     */
    public static void dispose() {
        painter.dispose();
    }
}
