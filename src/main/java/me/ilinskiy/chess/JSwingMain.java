package me.ilinskiy.chess;

import me.ilinskiy.chess.chessboard.PieceColor;
import me.ilinskiy.chess.game.GameRunner;
import me.ilinskiy.chess.game.Player;
import me.ilinskiy.chess.ui.JSwingChessPainter;
import me.ilinskiy.chess.ui.JSwingUserPlayer;

@SuppressWarnings("WeakerAccess")
public class JSwingMain {

    public static void main(String[] args) {
        Player p1 = new JSwingUserPlayer(PieceColor.White);
        Player p2 = new JSwingUserPlayer(PieceColor.Black);
        GameRunner gameRunner = new GameRunner(new JSwingChessPainter());
        gameRunner.askTimeOut();
        do {
            gameRunner.runGame(p1, p2);
        } while (gameRunner.askToPlayAgain());
        gameRunner.dispose();
    }
}
