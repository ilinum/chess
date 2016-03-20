package me.ilinskiy.chess;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.game.GameRunnerImpl;
import me.ilinskiy.chess.impl.ui.JSwingChessPainter;
import me.ilinskiy.chess.impl.ui.JSwingUserPlayer;

@SuppressWarnings("WeakerAccess")
public class JSwingMain {

    public static void main(String[] args) {
        Player p1 = new JSwingUserPlayer(PieceColor.White);
        Player p2 = new JSwingUserPlayer(PieceColor.Black);
        GameRunner gameRunner = new GameRunnerImpl(new JSwingChessPainter());
        gameRunner.askTimeOut();
        do {
            gameRunner.runGame(p1, p2);
        } while (gameRunner.askToPlayAgain());
        gameRunner.dispose();
    }
}
