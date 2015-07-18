package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import org.jetbrains.annotations.NotNull;

import static me.ilinskiy.chessBoard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static synchronized boolean makeMoveAndEvaluate(@NotNull ImmutableBoard b, @NotNull Move m, @NotNull BoardOperation op) {
        Board board = new Board(b);
        board.movePiece(m);
        boolean result = op.run(board.getInner());
        board.movePiece(m.inverse());
        return result;
    }
}
