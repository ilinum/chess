package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.BoardOperation;
import me.ilinskiy.chess.api.chessboard.BoardWrapper;
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

    public static <V> V makeMoveAndEvaluate(@NotNull Board b, @NotNull Move m,
                                            @NotNull BoardOperation<V> op) {
        BoardWrapper board = BoardWrapperImpl.getCopy(b);
        board.setPieceAccordingToMove(m);
        return op.run(board.getInner());
    }
}
