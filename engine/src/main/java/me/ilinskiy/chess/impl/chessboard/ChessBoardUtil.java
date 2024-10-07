package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.BoardOperation;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

import static me.ilinskiy.chess.api.chessboard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static <V> V makeMoveAndEvaluate(
            @NotNull MoveAwareBoard b, @NotNull Move m,
            @NotNull BoardOperation<V> op) {
        b = b.copy();
        b.makeMove(m);
        return op.run(b);
    }
}
