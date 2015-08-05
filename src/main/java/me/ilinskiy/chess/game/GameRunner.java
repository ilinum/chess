package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.ImmutableBoard;
import me.ilinskiy.chess.chessBoard.PieceColor;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
public class GameRunner {
    public static final int INIT_HEIGHT_AND_WIDTH = 62 * ImmutableBoard.BOARD_SIZE; //approx 500
    //public static final int MIN_HEIGHT_AND_WIDTH = 30 * ImmutableBoard.BOARD_SIZE;

    @NotNull
    public static PieceColor runGame(@NotNull Player p1, @NotNull Player p2) {
        JFrame game = new JFrame();
        game.setTitle("Chess");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setLayout(new BorderLayout());
        game.setSize(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH);
        //game.setMinimumSize(new Dimension(MIN_HEIGHT_AND_WIDTH, MIN_HEIGHT_AND_WIDTH));
        //game.setPreferredSize(new Dimension(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH));
        game.setResizable(false);
        Game g = new Game(p1, p2, game);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        while (!g.isGameOver()) {
            try {
                g.makeMove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return g.getWinner().get();
    }
}
