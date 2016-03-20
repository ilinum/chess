package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.Player;
import org.jetbrains.annotations.NotNull;

import static me.ilinskiy.chess.api.chessboard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static <V> V makeMoveAndEvaluate(@NotNull Board b, @NotNull Move m,
                                            @NotNull BoardOperation<V> op) {
        BoardWrapper board = BoardWrapperImpl.getCopy(b);
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
     * @deprecated use {@link BoardWrapperImpl#getCopy(Board)} instead
     */
    @NotNull
    @Deprecated
    public static BoardWrapper getBoardWrapperCopy(Board b) {
        return BoardWrapperImpl.getCopy(b);
    }
}
