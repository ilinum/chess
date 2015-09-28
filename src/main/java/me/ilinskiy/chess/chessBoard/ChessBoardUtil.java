package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Move;
import me.ilinskiy.chess.game.Player;

import static me.ilinskiy.chess.chessBoard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static <V> V makeMoveAndEvaluate(@NotNull Board b, @NotNull Move m,
                                            @NotNull BoardOperation<V> op) {
        BoardWrapper board = getBoardWrapperCopy(b);
        board.setPieceAccordingToMove(m);
        return op.run(board.getInner());
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

    public static boolean isOutOfBounds(@NotNull Coordinates c) {
        return c.getX() < 0 || c.getX() >= Board.BOARD_SIZE || c.getY() < 0 || c.getY() >= Board.BOARD_SIZE;
    }

    @NotNull
    public static BoardWrapper getBoardWrapperCopy(Board b) {
        return new BoardWrapper(b.copy());
    }
}
