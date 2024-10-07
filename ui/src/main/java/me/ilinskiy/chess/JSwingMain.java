package me.ilinskiy.chess;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.chessboard.MoveAwareBoardImpl;
import me.ilinskiy.chess.impl.game.GameRunnerImpl;
import me.ilinskiy.chess.ui.JSwingChessPainter;
import me.ilinskiy.chess.ui.JSwingUserPlayer;

public class JSwingMain {
    public static void main(String[] args) {
        JSwingChessPainter painter = null;
        do {
            if (painter != null) {
                painter.dispose();
            }
            painter = new JSwingChessPainter(new MoveAwareBoardImpl());
            Player p1 = new JSwingUserPlayer(PieceColor.White, painter);
            Player p2 = new JSwingUserPlayer(PieceColor.Black, painter);
            GameRunner gameRunner = new GameRunnerImpl(painter.askTimeOut());
            PieceColor winner = gameRunner.runGame(p1, p2);
            painter.gameOver(winner);
        } while (painter.askToPlayAgain());
        painter.dispose();
    }
}
