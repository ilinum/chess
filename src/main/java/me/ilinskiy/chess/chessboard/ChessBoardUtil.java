package me.ilinskiy.chess.chessboard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Player;
import me.ilinskiy.chess.game.moves.Move;

import static me.ilinskiy.chess.chessboard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static <V> V makeMoveAndEvaluate(@NotNull Board b, @NotNull Move m,
                                            @NotNull BoardOperation<V> op) {
        BoardWrapper board = BoardWrapper.getCopy(b);
        board.setPieceAccordingToMove(m);
        return op.run(board.getInner());
    }

    /**
     * @deprecated use {@link PieceColor#inverse()} instead
     */
    @NotNull
    @Deprecated
    public static PieceColor inverse(PieceColor color) {
        return color.inverse();
    }

    /**
     * @deprecated use {@link Player#inverse} instead
     */
    @NotNull
    @Deprecated
    public static Player inverse(@NotNull Player inverseOf, @NotNull Player p1, @NotNull Player p2) {
        return inverseOf.inverse(p1, p2);
    }

    /**
     * @deprecated use {@link Coordinates#isOutOfBounds} instead
     */
    @Deprecated
    public static boolean isOutOfBounds(@NotNull Coordinates c) {
        return c.isOutOfBounds();
    }

    /**
     * @deprecated use {@link BoardWrapper#getCopy(Board)} instead
     */
    @NotNull
    @Deprecated
    public static BoardWrapper getBoardWrapperCopy(Board b) {
        return BoardWrapper.getCopy(b);
    }
}
