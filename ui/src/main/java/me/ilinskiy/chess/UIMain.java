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
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;

@Command(name = "chess-ui", mixinStandardHelpOptions = true)
public class UIMain implements Runnable {

    @Option(
            names = {"--engine-port-white", "-w"},
            description = "The port of the UCI engine to talk to for the white player."
    )
    private int enginePortWhite = 0;

    @Option(
            names = {"--engine-port-black", "-b"},
            description = "The port of the UCI engine to talk to for the black player."
    )
    private int enginePortBlack = 0;

    @Option(
            names = {"--turn-timeout-secs", "-t"},
            description = "The maximum number of seconds that a player can take on a turn. " +
                    "Only applies to human players. Default is no timeout."
    )
    private int timeoutSeconds = 0;

    @Override
    public void run() {
        JSwingChessPainter painter = null;
        do {
            if (painter != null) {
                painter.dispose();
            }
            painter = new JSwingChessPainter(new MoveAwareBoardImpl(), timeoutSeconds);
            Player p1 = getPlayer(enginePortWhite, PieceColor.White, painter);
            Player p2 = getPlayer(enginePortBlack, PieceColor.Black, painter);
            GameRunner gameRunner = getGameRunner(painter);
            PieceColor winner = gameRunner.runGame(p1, p2);
            painter.gameOver(winner);
        } while (painter.askToPlayAgain());
        painter.dispose();
    }

    @NotNull
    private GameRunner getGameRunner(@NotNull JSwingChessPainter painter) {
        if (enginePortWhite != 0 && enginePortBlack != 0) {
            // Provide a callback, so we can monitor a game between two engines on the UI.
            return new GameRunnerImpl(timeoutSeconds, board -> {
                painter.updateBoard(board);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                return null;
            });
        }
        return new GameRunnerImpl(timeoutSeconds);
    }

    private Player getPlayer(int enginePort, PieceColor color, @NotNull JSwingChessPainter painter) {
        if (enginePort != 0) {
            UCIClient uci;
            try {
                uci = new UCIClient(enginePort);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            return new UCIPlayer(color, uci);
        }
        return new JSwingUserPlayer(color, painter);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new UIMain()).execute(args);
        System.exit(exitCode);
    }
}
