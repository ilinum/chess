package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static me.ilinskiy.chess.chessBoard.ImmutableBoard.*;
import static me.ilinskiy.chess.chessBoard.PieceColor.Black;
import static me.ilinskiy.chess.chessBoard.PieceColor.White;

/**
 * TODO: cache everything in this class
 * <p>
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class GameUtil {

    @NotNull
    public static synchronized List<Move> getAvailableMoves(@NotNull PieceColor color, @NotNull ImmutableBoard board) {
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
     */
    @NotNull
    public static List<Move> getAvailableMovesForPiece(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        //todo: use set instead of list?
        List<Move> result = getAllMovesForPiece(pos, board);

        //filter out the ones when king is attacked
        Iterator<Move> moveIterator = result.iterator();
        while (moveIterator.hasNext()) {
            if (kingIsAttackedAfterMove(board.getPieceAt(pos).getColor(), board, moveIterator.next())) {
                moveIterator.remove();
            }
        }
        return result;
    }

    /**
     * Get all available moves for piece without considering if the king will be attacked
     */
    @NotNull
    private static List<Move> getAllMovesForPiece(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
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
                result = getBishopMoves(pos, board);
                break;
            case Rook:
                result = getRookMoves(pos, board);
                break;
            case Queen:
                result = getRookMoves(pos, board);
                result.addAll(getBishopMoves(pos, board));
                break;
            case King:
                result = getKingMoves(pos, board);
                break;
            case Empty:
                break;
        }
        return result;
    }

    @NotNull
    private static List<Move> getRookMoves(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        int[] xChange = new int[]{-1, -1, 1, 1};
        int[] yChange = new int[]{1, -1, 1, -1};
        return getBishopOrRookMoves(pos, board, xChange, yChange);
    }

    @NotNull
    private static List<Move> getBishopMoves(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        int[] xChange = new int[]{0, 0, 1, -1};
        int[] yChange = new int[]{1, -1, 0, 0};
        return getBishopOrRookMoves(pos, board, xChange, yChange);
    }

    private static List<Move> getBishopOrRookMoves(@NotNull Coordinates pos, @NotNull ImmutableBoard board,
                                                   @NotNull int[] xChange, @NotNull int[] yChange) {
        assert xChange.length == yChange.length;
        List<Move> result = new ArrayList<>();
        assert board.getPieceAt(pos) instanceof Piece;
        Piece p = (Piece) board.getPieceAt(pos);
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new Coordinates(pos.getX() + xChange[i], pos.getY() + yChange[i]);
            while (!ChessBoardUtil.isOutOfBounds(c) && board.getPieceAt(c) instanceof EmptyCell) {
                result.add(new Move(pos, c));
            }
            if (!ChessBoardUtil.isOutOfBounds(c)) {
                PieceColor color = board.getPieceAt(c).getColor();
                if (!ChessBoardUtil.isOutOfBounds(c) && color == ChessBoardUtil.inverse(p.getColor())) {
                    result.add(new Move(pos, c));
                }
            }
        }
        return result;
    }

    @NotNull
    private static List<Move> getKingMoves(@NotNull Coordinates pos, @NotNull ImmutableBoard board) {
        int[] xChange = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] yChange = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        List<Move> result = new ArrayList<>();
        assert xChange.length == yChange.length;
        assert board.getPieceAt(pos) instanceof Piece;
        PieceColor kingColor = board.getPieceAt(pos).getColor();
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new Coordinates(pos.getX() + xChange[i], pos.getY() + yChange[i]);
            if (!ChessBoardUtil.isOutOfBounds(c)) {
                boolean correctColor = board.getPieceAt(c).getColor() != kingColor;
                if (correctColor && !kingAround(c, board, ChessBoardUtil.inverse(kingColor))) {
                    result.add(new Move(pos, c)); //all the situation when king is attacked will be filtered out later
                }
            }

        }
        return result;
    }

    private static boolean kingAround(@NotNull Coordinates pos, @NotNull ImmutableBoard board, PieceColor opposingKingColor) {
        int[] xChange = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] yChange = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        assert xChange.length == yChange.length;
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new Coordinates(pos.getX() + xChange[i], pos.getY() + yChange[i]);
            if (!ChessBoardUtil.isOutOfBounds(c)) {
                ChessElement elem = board.getPieceAt(c);
                if (elem.getType() == PieceType.King && elem.getColor() == opposingKingColor) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean kingIsAttacked(@NotNull PieceColor kingColor, @NotNull ImmutableBoard board) {
        List<Coordinates> allOpponentPieces = getAllPieces(ChessBoardUtil.inverse(kingColor), board);
        Coordinates kingPos = findKing(kingColor, board);

        for (Coordinates pos : allOpponentPieces) {
            List<Move> availableMovesForPiece = getAllMovesForPiece(pos, board);
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
