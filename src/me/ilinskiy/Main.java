package me.ilinskiy;

import me.ilinskiy.chessBoard.ImmutableBoard;
import me.ilinskiy.game.Game;
import me.ilinskiy.game.Player;
import me.ilinskiy.game.PlayerMock;
import me.ilinskiy.game.UserPlayer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static final int INIT_HEIGHT_AND_WIDTH = 62 * ImmutableBoard.BOARD_SIZE; //approx 500
    public static final int MIN_HEIGHT_AND_WIDTH = 30 * ImmutableBoard.BOARD_SIZE;

    public static void main(String[] args) {
        JFrame game = new JFrame();
        game.setTitle("Chess");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Player p1 = new UserPlayer();
        Player p2 = new PlayerMock();
        game.setLayout(new BorderLayout());
        game.setSize(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH);
//        game.setMinimumSize(new Dimension(MIN_HEIGHT_AND_WIDTH, MIN_HEIGHT_AND_WIDTH));
//        game.setPreferredSize(new Dimension(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH));
        game.setResizable(false); //todo: allow resizing
        Game g = new Game(p1, p2, game);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        while (true) {
            try {
                g.makeMove();
            } catch (Exception ignored) {

            }
        }
    }
}
