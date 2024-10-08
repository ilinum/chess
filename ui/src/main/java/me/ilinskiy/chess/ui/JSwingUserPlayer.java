package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.game.BoardAnalyzer;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public final class JSwingUserPlayer implements Player {
    private final PieceColor myColor;
    private final JSwingChessPainter painter;

    public JSwingUserPlayer(PieceColor color, JSwingChessPainter painter) {
        myColor = color;
        this.painter = painter;
    }

    @NotNull
    @Override
    public Move getMove(@NotNull MoveAwareBoard board) {
        painter.moveStarted();
        BoardPanel panel = painter.getPanel();
        panel.setBoard(board);
        this.painter.repaint();

        CellSelectListener mouseListener = new CellSelectListener(panel);
        panel.addMouseListener(mouseListener);
        Move move = null;
        try {
            while (move == null) {
                Coordinates selected = painter.getSelected();
                Coordinates location = mouseListener.waitForSelection();
                if (location == null) {
                    // Try getting location again.
                    continue;
                }
                if (selected != null) {
                    Set<Move> availableMoves = BoardAnalyzer.getAvailableMovesForPiece(selected, board);
                    move = getMoveFromSelection(selected, location, availableMoves);
                    if (move == null && board.getPiece(location).getColor() == myColor) {
                        painter.setSelected(location);
                    }
                } else if (board.getPiece(location).getColor() == myColor) {
                    painter.setSelected(location);
                }
            }
            panel.setSelected(null);
            painter.moveFinished();
            return move;
        } finally {
            assert panel.getMouseListeners().length > 0;
            panel.removeMouseListener(mouseListener);
            assert panel.getMouseListeners().length == 0;
        }
    }

    @Nullable
    private static Move getMoveFromSelection(
            @NotNull Coordinates moveStart,
            @NotNull Coordinates moveEnd,
            @NotNull Set<Move> availableMovesForPiece) {
        List<Move> possibleMoves = new ArrayList<>();
        for (Move availableMove : availableMovesForPiece) {
            if (availableMove.getInitialPositions()[0].equals(moveStart) &&
                    availableMove.getNewPositions()[0].equals(moveEnd)) {
                possibleMoves.add(availableMove);
            }
        }
        if (possibleMoves.isEmpty()) {
            return null;
        }
        if (possibleMoves.size() > 1) {
            // Must be promotion!
            PieceType piece = new ChoosePieceTypeForPromotedPawn().getChosenPiece();
            for (Move possibleMove : possibleMoves) {
                if (possibleMove instanceof PawnPromotion pp && pp.promoteTo == piece) {
                    return possibleMove;
                }
            }
        }
        return possibleMoves.getFirst();
    }

    @NotNull
    @Override
    public PieceColor getColor() {
        return myColor;
    }
}
