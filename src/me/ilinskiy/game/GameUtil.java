package me.ilinskiy.game;

import me.ilinskiy.chessBoard.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static me.ilinskiy.chessBoard.ImmutableBoard.*;
import static me.ilinskiy.chessBoard.PieceColor.Black;
import static me.ilinskiy.chessBoard.PieceColor.White;

/**
 * TODO: cache everything in this class
 * <p>
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class GameUtil {

    @NotNull
    public static List<Move> getAvailableMoves(@NotNull PieceColor color, @NotNull ImmutableBoard board) {
        List<Move> result = new ArrayList<>();
        List<Coordinates> allPiecesOfCorrectColor = getAllPieces(color, board);

        for (Coordinates pos : allPiecesOfCorrectColor) {
            result.addAll(getAvailableMovesForPiece(pos, board));
        }

        return result;
    }

    @NotNull
    public static List<Coordinates> getAllPieces(PieceColor color, @NotNull ImmutableBoard board) {
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

    public static int getDirectionForPlayer(@NotNull PieceColor color) {
        if (color == PieceColor.White) {
            return WHITE_DIRECTION;
        } else {
            assert color == PieceColor.Black;
            return BLACK_DIRECTION;
        }
    }

    /**
     * If board.getPieceAt(pos) is EmptyCell returns an empty list
     * <p>
     * todo: move this to ImmutableBoard class???
     */
    @NotNull
    public static List<Move> getAvailableMovesForPiece(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {

        List<Move> result = getAvailableMovesForPieceWithoutKingAttacked(pos, board);

        //filter out the ones when king is attacked
        Iterator<Move> moveIterator = result.iterator();
        while (moveIterator.hasNext()) {
            if (kingIsAttackedAfterMove(board.getPieceAt(pos).getColor(), board, moveIterator.next())) {
                moveIterator.remove();
            }
        }
        return result;
    }

    @NotNull
    private static List<Move> getAvailableMovesForPieceWithoutKingAttacked(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        ChessElement element = board.getPieceAt(pos);
        List<Move> result = new LinkedList<>();
        PieceColor color = element.getColor();
        switch (element.getType()) {
            case Pawn:
                assert (color == White || color == Black);
                int dir = getDirectionForPlayer(color);
                assert !ChessBoardUtil.isOutOfBounds(new Coordinates(pos.getX(), pos.getY() + dir)); //should've been promoted
                result.add(new Move(pos, new Coordinates(pos.getX(), pos.getY() + dir)));
                boolean hasNotMoved = ChessBoardUtil.isOutOfBounds(new Coordinates(pos.getX(), pos.getY() - 2 * dir));
                if (hasNotMoved) {
                    result.add(new Move(pos, new Coordinates(pos.getX(), pos.getY() + 2 * dir)));
                }
                Coordinates[] eatLocations = new Coordinates[]{new Coordinates(pos.getX() + 1, pos.getY() + dir),
                        new Coordinates(pos.getX() - 1, pos.getY() + dir)};
                for (Coordinates eatLocation : eatLocations) {
                    boolean outOfBounds = ChessBoardUtil.isOutOfBounds(eatLocation);
                    if (!outOfBounds && board.getPieceAt(eatLocation).getColor() == ChessBoardUtil.inverse(color)) {
                        result.add(new Move(pos, eatLocation));
                    }
                }
                //todo: allow En passant
                break;
            case Knight:
                int[] xChange = new int[]{1, -1, 2, 2, -2, -2, 1, -1};
                int[] yChange = new int[]{2, 2, 1, -1, 1, -1, -2, -2};
                assert xChange.length == yChange.length;
                for (int c = 0; c < xChange.length; c++) {
                    Coordinates newPos = new Coordinates(pos.getX() + xChange[c], pos.getY() + yChange[c]);
                    if (!ChessBoardUtil.isOutOfBounds(newPos) && board.getPieceAt(newPos).getColor() != color) {
                        result.add(new Move(pos, newPos));
                    }
                }
                break;
            case Bishop:
                //xChange = new int[]{};
                break;
            case Rook:
                break;
            case Queen:
                break;
            case King:
                //don't forget to add castling!
                break;
            case Empty:
                break;
        }
        return result;
        //        throw new NotImplementedException(); //todo: implement this method
    }

    public static boolean kingIsAttacked(@NotNull PieceColor kingColor, @NotNull ImmutableBoard board) {
        List<Coordinates> allOpponentPieces = getAllPieces(ChessBoardUtil.inverse(kingColor), board);
        Coordinates kingPos = findKing(kingColor, board);

        for (Coordinates pos : allOpponentPieces) {
            List<Move> availableMovesForPiece = getAvailableMovesForPieceWithoutKingAttacked(pos, board);
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
        assert kingLocation.size() == 1 : "Wrong number of kings on the board: " + kingLocation;
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
