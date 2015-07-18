package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import me.ilinskiy.game.Player;
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

    @NotNull
    public static PieceColor inverse(PieceColor color) {
        if (color == PieceColor.Empty) {
            throw new IllegalArgumentException("Color may not be empty!");
        }
        return color == PieceColor.Black ? PieceColor.White : PieceColor.Black;
    }

    @NotNull
    public static Player inverse(@NotNull Player inverseOf, @NotNull Player p1, @NotNull Player p2) {
        if (inverseOf.getPlayerColor() == PieceColor.Empty) {
            throw new IllegalArgumentException("Color may not be empty!");
        }
        return inverseOf == p1 ? p2 : p1;
    }
}
