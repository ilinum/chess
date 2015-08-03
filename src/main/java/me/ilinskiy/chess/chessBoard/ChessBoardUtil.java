package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Move;
import me.ilinskiy.chess.game.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static me.ilinskiy.chess.chessBoard.PieceType.*;

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
        File[] images = new File[] {
                new File("src/main/resources/ChessPieces/white-pawn.png"),
                new File("src/main/resources/ChessPieces/white-knight.png"),
                new File("src/main/resources/ChessPieces/white-bishop.png"),
                new File("src/main/resources/ChessPieces/white-rook.png"),
                new File("src/main/resources/ChessPieces/white-queen.png"),
                new File("src/main/resources/ChessPieces/white-king.png"),
                new File("src/main/resources/ChessPieces/black-pawn.png"),
                new File("src/main/resources/ChessPieces/black-knight.png"),
                new File("src/main/resources/ChessPieces/black-bishop.png"),
                new File("src/main/resources/ChessPieces/black-rook.png"),
                new File("src/main/resources/ChessPieces/black-queen.png"),
                new File("src/main/resources/ChessPieces/black-king.png")
        };
        Piece[] pieces = new Piece[] {
                new Piece(PieceColor.White, Pawn),
                new Piece(PieceColor.White, Knight),
                new Piece(PieceColor.White, Bishop),
                new Piece(PieceColor.White, Rook),
                new Piece(PieceColor.White, Queen),
                new Piece(PieceColor.White, King),
                new Piece(PieceColor.Black, Pawn),
                new Piece(PieceColor.Black, Knight),
                new Piece(PieceColor.Black, Bishop),
                new Piece(PieceColor.Black, Rook),
                new Piece(PieceColor.Black, Queen),
                new Piece(PieceColor.Black, King)
        };
        assert pieces.length == images.length;
        for (int i = 0; i < pieces.length; i++) {
            File img = images[i];
            if (img.exists()) {
                try {
                    BufferedImage bf = ImageIO.read(img);
                    icons.put(pieces[i], new ImageIcon(bf).getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File not found: " + img.getAbsolutePath());
            }
        }
    }

    public static boolean isOutOfBounds(@NotNull Coordinates c) {
        return c.getX() < 0 || c.getX() >= ImmutableBoard.BOARD_SIZE || c.getY() < 0 || c.getY() >= ImmutableBoard.BOARD_SIZE;
    }
}
