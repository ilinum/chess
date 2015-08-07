package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Board;
import me.ilinskiy.chess.chessBoard.PieceColor;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunner {
    public static final int INIT_HEIGHT_AND_WIDTH = 62 * Board.BOARD_SIZE; //approx 500
    private static JFrame game;
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
        if (game == null) {
            game = new JFrame();
            game.setTitle("Chess");
            game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            game.setResizable(false);
            game.setLayout(new BorderLayout());
            game.setSize(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH);
            game.getContentPane().setPreferredSize(new Dimension(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH));
            game.pack();
            g = new Game(p1, p2, game);
            game.setLocationRelativeTo(null);
            game.setVisible(true);
        } else {
            game.getContentPane().removeAll();
            g = new Game(p1, p2, game);
            game.revalidate();
            game.repaint();
        }
        while (!g.isGameOver()) {
            try {
                g.makeMove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PieceColor winner = g.getWinner().get();
        JOptionPane.showMessageDialog(game, getWinPhrase(winner));
        return winner;
    }

    @NotNull
    private static String getWinPhrase(@NotNull PieceColor winner) {
        switch (winner) {
            case Black:
                return "Black won!";
            case White:
                return "White won!";
            case Empty:
                return "It's a draw!";
            default:
                throw new IllegalArgumentException("Something went wrong");
        }
    }

    /**
     * creates a dialog that asks if user wants to play again
     *
     * @return 0 if user pressed yes
     */
    public static int askToPlayAgain() {
        JFrame frame;
        if (game != null) {
            frame = game;
        } else {
            frame = new JFrame();
        }
        return JOptionPane.showConfirmDialog(frame, "Would you like to play again?");
    }

    /**
     * Ask the user the timeout they want to use. It sets the timeout according to the answer
     */
    public static void askTimeOut() {
        JFrame frame = new JFrame();

        boolean updated = false;
        while (!updated) {
            String input = JOptionPane.showInputDialog(frame, "Enter timeout in seconds \n(0 for no timeout)",
                    "Timeout", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                updated = true;
            } else {
                try {
                    int newTimeout = Integer.parseInt(input);
                    if (newTimeout >= 0) {
                        TIMEOUT_IN_SECONDS = newTimeout;
                        updated = true;
                    }
                } catch (NumberFormatException ignored) {

                }
            }
        }
    }

    /**
     * Disposes of the frame
     */
    public static void dispose() {
        if (game != null) {
            game.dispose();
            game = null;
        }
    }
}
