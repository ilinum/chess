package me.ilinskiy.game;

import me.ilinskiy.chessBoard.*;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static me.ilinskiy.chessBoard.ImmutableBoard.BOARD_SIZE;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class GameUtil {

    @NotNull
    public static List<Move> getAvailableMoves(@NotNull Player player, @NotNull ImmutableBoard board) {
        List<Move> result = new ArrayList<>();
        ArrayList<Coordinates> allPiecesOfCorrectColor = new ArrayList<>(BOARD_SIZE * 2); //should use default instead?
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates c = new Coordinates(row, col);
                if (board.getPieceAt(c).getColor() == player.getPlayerColor()) {
                    allPiecesOfCorrectColor.add(c);
                }
            }
        }

        for (Coordinates pos : allPiecesOfCorrectColor) {
            result.addAll(getAvailableMovesForPiece(pos, board));
        }
        if (kingIsAttacked(player.getPlayerColor(), board)) {
            List<Move> actualResult = new ArrayList<>(result.size());
            result.forEach(move -> {
                if (!kingIsAttackedAfterMove(player.getPlayerColor(), board, move)) {
                    actualResult.add(move);
                }
            });
            result = actualResult;
        }
        return result;
    }

    @NotNull
    public static List<Move> getAvailableMovesForPiece(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        throw new NotImplementedException(); //todo: implement this method
    }

    public static boolean kingIsAttacked(@NotNull PieceColor color, @NotNull ImmutableBoard board) {
        throw new NotImplementedException(); //todo: implement this method
    }

    public static boolean kingIsAttackedAfterMove(@NotNull PieceColor color, @NotNull ImmutableBoard b, @NotNull Move m) {
        return ChessBoardUtil.makeMoveAndEvaluate(b, m, board -> kingIsAttacked(color, board));
    }

    @NotNull
    public static Coordinates findKing(@NotNull PieceColor kingColor, @NotNull ImmutableBoard board) {
        List<Coordinates> kingLocation = findPieceByTypeAndColor(PieceType.King, kingColor, board);
        if (kingLocation.size() != 1) {
            String message = "Wrong number of " + kingColor.toString() + " on the board: " + kingLocation.size();
            throw new RuntimeException(message);
        }
        return kingLocation.get(0);
    }

    @NotNull
    public static List<Coordinates> findPieceByTypeAndColor(@NotNull PieceType type, @NotNull PieceColor color,
                                                            @NotNull ImmutableBoard board) {
        ChessElement elemToFind = new Piece(color, type);
        List<Coordinates> result = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.getPieceAt(new Coordinates(i, j)).equals(elemToFind)) {
                    result.add(new Coordinates(i, j));
                }
            }
        }
        return result;
    }
}
