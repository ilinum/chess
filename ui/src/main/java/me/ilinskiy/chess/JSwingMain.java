package me.ilinskiy.chess;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.GameRunner;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.chessboard.MoveAwareBoardImpl;
import me.ilinskiy.chess.impl.game.GameRunnerImpl;
import me.ilinskiy.chess.uci.UCIClient;
import me.ilinskiy.chess.uci.UCIPlayer;
import me.ilinskiy.chess.ui.JSwingChessPainter;
import me.ilinskiy.chess.ui.JSwingUserPlayer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;

@Command(name = "chess-ui", mixinStandardHelpOptions = true)
public class JSwingMain implements Runnable {

    @Option(
            names = {"--pipe-prefix", "-p"},
            description = "The prefix of the pipe that the engine uses that this UI should communicate with. " +
                    "The actual pipes will be <prefix>_in and <prefix>_out. If not specified, " +
                    "plays with two players using the same GUI.")
    private String pipePrefix = "";

    @Override
    public void run() {
        JSwingChessPainter painter = null;
        do {
            if (painter != null) {
                painter.dispose();
            }
            painter = new JSwingChessPainter(new MoveAwareBoardImpl());
            Player p1 = new JSwingUserPlayer(PieceColor.White, painter);
            Player p2;
            if (!pipePrefix.isEmpty()) {
                UCIClient uci;
                try {
                    uci = new UCIClient(pipePrefix + "_out", pipePrefix + "_in");
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
                p2 = new UCIPlayer(PieceColor.Black, uci);
            } else {
                p2 = new JSwingUserPlayer(PieceColor.Black, painter);
            }
            GameRunner gameRunner = new GameRunnerImpl(painter.askTimeOut());
            PieceColor winner = gameRunner.runGame(p1, p2);
            painter.gameOver(winner);
        } while (painter.askToPlayAgain());
        painter.dispose();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JSwingMain()).execute(args);
        System.exit(exitCode);
    }
}
