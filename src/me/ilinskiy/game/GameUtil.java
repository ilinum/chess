package me.ilinskiy.game;

import me.ilinskiy.chessBoard.*;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static me.ilinskiy.chessBoard.ImmutableBoard.BOARD_SIZE;

/**
 * TODO: cache everything in this class
 *
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class GameUtil {

    @NotNull
    public static List<Move> getAvailableMoves(@NotNull PieceColor color, @NotNull ImmutableBoard board) {
        List<Move> result = new LinkedList<>();
        List<Coordinates> allPiecesOfCorrectColor = getAllPieces(color, board);

        for (Coordinates pos : allPiecesOfCorrectColor) {
            result.addAll(getAvailableMovesForPiece(pos, board));
        }

        Iterator<Move> moveIterator = result.iterator();
        while (moveIterator.hasNext()) {
            if (kingIsAttackedAfterMove(color, board, moveIterator.next())) {
                moveIterator.remove();
            }
        }
        return result;
    }

    @NotNull
    private static List<Coordinates> getAllPieces(PieceColor color, @NotNull ImmutableBoard board) {
        ArrayList<Coordinates> allPiecesOfCorrectColor = new ArrayList<>(BOARD_SIZE * 2); //should use default instead?
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates c = new Coordinates(row, col);
                if (board.getPieceAt(c).getColor() == color) {
                    allPiecesOfCorrectColor.add(c);
                }
            }
        }
        return allPiecesOfCorrectColor;
    }

    public static List<Move> getAvailableMovesForPiece(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        throw new NotImplementedException(); //todo: implement this method
    }

    public static boolean kingIsAttacked(@NotNull PieceColor kingColor, @NotNull ImmutableBoard board) {
        List<Coordinates> allOpponentPieces = getAllPieces(ChessBoardUtil.inverse(kingColor), board);
        Coordinates kingPos = findKing(kingColor, board);

        for (Coordinates pos : allOpponentPieces) {
            List<Move> availableMovesForPiece = getAvailableMovesForPiece(pos, board);
            for (Move m : availableMovesForPiece) {
                if (m.getNewPosition().equals(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean kingIsAttackedAfterMove(@NotNull PieceColor color, @NotNull ImmutableBoard b,
                                                   @NotNull Move m) {
        return ChessBoardUtil.makeMoveAndEvaluate(b, m, board -> kingIsAttacked(color, board));
    }

    @NotNull
    private static Coordinates findKing(@NotNull PieceColor kingColor, @NotNull ImmutableBoard board) {
        List<Coordinates> kingLocation = findPiecesByTypeAndColor(PieceType.King, kingColor, board);
        if (kingLocation.size() != 1) {
            String message = "Wrong number of " + kingColor.toString() + " on the board: " + kingLocation.size();
            throw new RuntimeException(message);
        }
        return kingLocation.get(0);
    }

    @NotNull
    public static List<Coordinates> findPiecesByTypeAndColor(@NotNull PieceType type, @NotNull PieceColor color,
                                                             @NotNull ImmutableBoard board) {
        ChessElement elemToFind = new Piece(color, type);
        List<Coordinates> result = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                if (board.getPieceAt(coordinates).equals(elemToFind)) {
                    result.add(coordinates);
                }
            }
        }
        return result;
    }
}
