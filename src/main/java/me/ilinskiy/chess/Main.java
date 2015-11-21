package me.ilinskiy.chess;

import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.game.GameRunner;
import me.ilinskiy.chess.game.Player;
import me.ilinskiy.chess.ui.JSwingUserPlayer;

@SuppressWarnings("WeakerAccess")
public class Main {

    public static void main(String[] args) {
        Player p1 = new JSwingUserPlayer(PieceColor.White);
        Player p2 = new JSwingUserPlayer(PieceColor.Black);
        GameRunner.askTimeOut();
        do {
            GameRunner.runGame(p1, p2);
        } while (GameRunner.askToPlayAgain());
        GameRunner.dispose();
    }
}
