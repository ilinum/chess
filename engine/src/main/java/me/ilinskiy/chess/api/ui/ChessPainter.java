package me.ilinskiy.chess.api.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 9/27/15.
 */
public interface ChessPainter {
    /**
     * Initialize the Painter with the board
     */
    void initialize(@NotNull Board board);

    void gameOver(@NotNull PieceColor winner);

    /**
     * @return true if user pressed yes
     */
    boolean askToPlayAgain();

    /**
     * Dispose of everything
     *
     * No other method will be called in this class after dispose.
     * At the same time, dispose is not guaranteed to be called
     */
    void dispose();

    /**
     * @return the desired timeout in seconds or 0 for default
     */
    int askTimeOut();

    void moveStarted();

    void moveFinished();

     /**
     * A particular cell has been change and needs to be repainted
     */
    void repaint();
}
