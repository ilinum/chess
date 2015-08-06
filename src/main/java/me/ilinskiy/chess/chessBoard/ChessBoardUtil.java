package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Move;
import me.ilinskiy.chess.game.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static me.ilinskiy.chess.chessBoard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ChessBoardUtil {
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};

    public static synchronized boolean makeMoveAndEvaluate(@NotNull Board b, @NotNull Move m,
                                                           @NotNull BoardOperation op) {
        BoardWrapper board = new BoardWrapper(b.copy());
        board.setPieceAccordingToMove(m);
        boolean result = op.run(board.getInner());
        board.setPieceAccordingToMove(m.inverse()); //roll back
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
    public static final HashMap<Piece, Image> icons = new HashMap<>();

    public static void initIcons() {
            InputStream[] images = new InputStream[]{
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-pawn.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-knight.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-bishop.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-rook.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-queen.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/white-king.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-pawn.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-knight.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-bishop.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-rook.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-queen.png"),
                    ChessBoardUtil.class.getResourceAsStream("/resources/ChessPieces/black-king.png")
            };
            Piece[] pieces = new Piece[]{
                    Piece.createPiece(PieceColor.White, Pawn),
                    Piece.createPiece(PieceColor.White, Knight),
                    Piece.createPiece(PieceColor.White, Bishop),
                    Piece.createPiece(PieceColor.White, Rook),
                    Piece.createPiece(PieceColor.White, Queen),
                    Piece.createPiece(PieceColor.White, King),
                    Piece.createPiece(PieceColor.Black, Pawn),
                    Piece.createPiece(PieceColor.Black, Knight),
                    Piece.createPiece(PieceColor.Black, Bishop),
                    Piece.createPiece(PieceColor.Black, Rook),
                    Piece.createPiece(PieceColor.Black, Queen),
                    Piece.createPiece(PieceColor.Black, King)
            };
            assert pieces.length == images.length;
            for (int i = 0; i < pieces.length; i++) {
                InputStream img = images[i];
                try {
                    BufferedImage bf = ImageIO.read(img);
                    icons.put(pieces[i], new ImageIcon(bf).getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public static boolean isOutOfBounds(@NotNull Coordinates c) {
        return c.getX() < 0 || c.getX() >= Board.BOARD_SIZE || c.getY() < 0 || c.getY() >= Board.BOARD_SIZE;
    }
}
