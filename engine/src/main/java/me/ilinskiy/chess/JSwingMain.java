package me.ilinskiy.chess;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.chessboard.BoardImpl;
import me.ilinskiy.chess.impl.chessboard.BoardWrapperImpl;
import me.ilinskiy.chess.impl.game.GameRunnerImpl;
import me.ilinskiy.chess.impl.ui.JSwingChessPainter;
import me.ilinskiy.chess.impl.ui.JSwingUserPlayer;

@SuppressWarnings("WeakerAccess")
public class JSwingMain {

    public static void main(String[] args) {
        JSwingChessPainter painter = null;
        do {
            if (painter != null) {
                painter.dispose();
            }
            painter = new JSwingChessPainter(new BoardWrapperImpl().getInner());
            Player p1 = new JSwingUserPlayer(PieceColor.White, painter);
            Player p2 = new JSwingUserPlayer(PieceColor.Black, painter);
            GameRunner gameRunner = new GameRunnerImpl(painter, painter.askTimeOut());
            PieceColor winner = gameRunner.runGame(p1, p2);
            painter.gameOver(winner);
        } while (painter.askToPlayAgain());
        painter.dispose();
    }
}
