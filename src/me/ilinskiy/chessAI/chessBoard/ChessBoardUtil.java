package me.ilinskiy.chessAI.chessBoard;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.game.Move;
import me.ilinskiy.chessAI.game.Player;

import java.awt.*;
import java.util.HashMap;

import static me.ilinskiy.chessAI.chessBoard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static synchronized boolean makeMoveAndEvaluate(@NotNull ImmutableBoard b, @NotNull Move m,
                                                           @NotNull BoardOperation op) {
        Board board = new Board(b.copy());
        board.movePiece(m);
        boolean result = op.run(board.getInner());
        board.movePiece(m.inverse()); //roll back
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

    @NotNull
    public static HashMap<Piece, Image> icons = new HashMap<>();

    public static void initIcons() {
        /*try {
            BufferedImage bf = ImageIO.read(new File("resources/ChessPieceIcons/white_pawn.jpg"));
            icons.put(new Piece(PieceColor.White, Pawn), new ImageIcon(bf).getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static boolean isOutOfBounds(@NotNull Coordinates c) {
        return c.getX() < 0 || c.getX() >= ImmutableBoard.BOARD_SIZE || c.getY() < 0 || c.getY() >= ImmutableBoard.BOARD_SIZE;
    }
}
