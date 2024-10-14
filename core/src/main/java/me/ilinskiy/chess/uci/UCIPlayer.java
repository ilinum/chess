package me.ilinskiy.chess.uci;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.game.BoardAnalyzer;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UCIPlayer implements Player {
    @NotNull
    private final PieceColor color;
    @NotNull
    private final UCIClient client;

    public UCIPlayer(@NotNull PieceColor color, @NotNull UCIClient client) {
        this.color = color;
        this.client = client;
    }

    @Override
    public @NotNull Move getMove(@NotNull MoveAwareBoard b) {
        List<String> moves = new ArrayList<>();
        for (Move m : b.getMoves()) {
            moves.add(m.getUCIString());
        }
        String move;
        try {
            move = this.client.getMove(moves);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        Coordinates start = Coordinates.fromUCINotation(move.substring(0, 2));
        Coordinates end = Coordinates.fromUCINotation(move.substring(2, 4));
        String promotionPiece = "";
        if (move.length() > 4) {
            // Promotion.
            promotionPiece = move.substring(4, 5).toUpperCase();
        }
        Set<Move> availableMoves = BoardAnalyzer.getAvailableMovesForPiece(start, b);
        for (Move availableMove : availableMoves) {
            if (availableMove.getNewPositions()[0].equals(end)) {
                if (availableMove instanceof PawnPromotion pp) {
                    if (pp.promoteTo.symbol().equals(promotionPiece)) {
                        return availableMove;
                    }
                } else {
                    return availableMove;
                }
            }
        }
        throw new RuntimeException("no matching move found: " + move);
    }

    @Override
    public @NotNull PieceColor getColor() {
        return color;
    }
}
