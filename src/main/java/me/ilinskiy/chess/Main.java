package me.ilinskiy.chess;

import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.game.GameRunner;
import me.ilinskiy.chess.game.Player;
import me.ilinskiy.chess.game.UserPlayer;

public class Main {

    public static void main(String[] args) {
        Player p1 = new UserPlayer(PieceColor.White);
        Player p2 = new UserPlayer(PieceColor.Black);
        PieceColor winner = GameRunner.runGame(p1, p2);
        if (winner == PieceColor.Empty) {
            System.out.printf("It's a draw!");
        } else {
            System.out.printf("%s wins!", winner);
        }
    }
}
