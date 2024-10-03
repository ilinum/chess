package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.ChessBoardUtil;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.chessboard.EmptyCell;
import me.ilinskiy.chess.impl.chessboard.Piece;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.ilinskiy.chess.api.chessboard.PieceColor.Black;
import static me.ilinskiy.chess.api.chessboard.PieceColor.White;
import static me.ilinskiy.chess.impl.chessboard.BoardImpl.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class GameUtil {

    @NotNull
    public static synchronized List<Move> getAvailableMoves(@NotNull PieceColor color, @NotNull Board board) {
        List<Move> result = new ArrayList<>();
        List<Coordinates> allPiecesOfCorrectColor = getAllPieces(color, board);

        for (Coordinates pos : allPiecesOfCorrectColor) {
            result.addAll(getAvailableMovesForPiece(pos, board));
        }

        return result;
    }

    @NotNull
    public static List<Coordinates> getAllPieces(PieceColor color, @NotNull Board board) {
        ArrayList<Coordinates> allPiecesOfCorrectColor = new ArrayList<>(BOARD_SIZE * 2); //should use default instead?
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates c = new CoordinatesImpl(row, col);
                if (board.getPieceAt(c).getColor() == color) {
                    allPiecesOfCorrectColor.add(c);
                }
            }
        }
        return allPiecesOfCorrectColor;
    }

    static int getDirectionForPlayer(@NotNull PieceColor color) {
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
    public static Set<Move> getAvailableMovesForPiece(@NotNull Coordinates pos, @NotNull Board board) {
        Set<Move> result = getAllMovesForPiece(pos, board);

        //filter out the ones when king is attacked
        Iterator<Move> moveIterator = result.iterator();
        while (moveIterator.hasNext()) {
            if (kingIsAttackedAfterMove(board.getPieceAt(pos).getColor(), board, moveIterator.next())) {
                moveIterator.remove();
            }
        }
        return result;
    }

    public static Set<Coordinates> getAvailableNewPositions(@NotNull Coordinates pos, @NotNull Board board) {
        Set<Move> moves = getAvailableMovesForPiece(pos, board);
        Set<Coordinates> res = new HashSet<>(moves.size());
        for (Move move : moves) {
            Collections.addAll(res, move.getNewPositions());
        }
        return res;
    }

    /**
     * Get all available moves for piece without considering if the king will be attacked
     */
    @NotNull
    private static Set<Move> getAllMovesForPiece(@NotNull Coordinates pos, @NotNull Board board) {
        ChessElement element = board.getPieceAt(pos);
        Set<Move> result = new HashSet<>();
        PieceColor color = element.getColor();
        switch (element.getType()) {
            case Pawn:
                assert (color == White || color == Black);
                int dir = getDirectionForPlayer(color);
                Coordinates newC = new CoordinatesImpl(pos.getX(), pos.getY() + dir);
                if (!newC.isOutOfBounds()) { //should've been promoted
                    if (board.getPieceAt(newC) instanceof EmptyCell) {
                        result.add(new RegularMove(pos, newC));
                        boolean hasNotMoved = new CoordinatesImpl(pos.getX(), pos.getY() - 2 * dir).isOutOfBounds();
                        Coordinates longMove = new CoordinatesImpl(pos.getX(), pos.getY() + 2 * dir);
                        if (hasNotMoved && (board.getPieceAt(longMove) instanceof EmptyCell)) {
                            result.add(new RegularMove(pos, longMove));
                        }
                    }
                    Coordinates[] eatLocations = new Coordinates[]{new CoordinatesImpl(pos.getX() + 1, pos.getY() + dir),
                            new CoordinatesImpl(pos.getX() - 1, pos.getY() + dir)};
                    for (Coordinates eatLocation : eatLocations) {
                        boolean outOfBounds = eatLocation.isOutOfBounds();
                        PieceColor enemyColor = color.inverse();
                        if (!outOfBounds && board.getPieceAt(eatLocation).getColor() == enemyColor) {
                            result.add(new RegularMove(pos, eatLocation));
                        } else {
                            Optional<Move> lastMove = board.getLastMove();
                            if (!outOfBounds && lastMove.isPresent()) { //check for en passe
                                @SuppressWarnings("OptionalGetWithoutIsPresent")
                                Move move = lastMove.get();
                                if (move instanceof RegularMove) {
                                    RegularMove rm = (RegularMove) move;
                                    Coordinates newPos = rm.newPosition;
                                    Coordinates initPos = rm.initialPosition;
                                    int enemyDir = getDirectionForPlayer(color.inverse());
                                    if (eatLocation.equals(new CoordinatesImpl(initPos.getX(), initPos.getY() + enemyDir))) {
                                        ChessElement piece = board.getPieceAt(newPos);
                                        if (piece.getType() == PieceType.Pawn && piece.getColor() == enemyColor) {
                                            boolean wasALongMove = Math.abs(initPos.getY() - newPos.getY()) == 2;
                                            if (wasALongMove) {
                                                Coordinates c = new CoordinatesImpl(initPos.getX(), initPos.getY() + enemyDir);
                                                result.add(new EnPasse(pos, c));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case Knight:
                int[] xChange = new int[]{1, -1, 2, 2, -2, -2, 1, -1};
                int[] yChange = new int[]{2, 2, 1, -1, 1, -1, -2, -2};
                assert xChange.length == yChange.length;
                for (int c = 0; c < xChange.length; c++) {
                    Coordinates newPos = new CoordinatesImpl(pos.getX() + xChange[c], pos.getY() + yChange[c]);
                    if (!newPos.isOutOfBounds() && board.getPieceAt(newPos).getColor() != color) {
                        result.add(new RegularMove(pos, newPos));
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
    private static Set<Move> getRookMoves(@NotNull Coordinates pos, @NotNull Board board) {
        int[] xChange = new int[]{0, 0, 1, -1};
        int[] yChange = new int[]{1, -1, 0, 0};
        return getBishopOrRookMoves(pos, board, xChange, yChange);
    }

    @NotNull
    private static Set<Move> getBishopMoves(@NotNull Coordinates pos, @NotNull Board board) {
        int[] xChange = new int[]{-1, -1, 1, 1};
        int[] yChange = new int[]{1, -1, 1, -1};
        return getBishopOrRookMoves(pos, board, xChange, yChange);
    }

    private static Set<Move> getBishopOrRookMoves(@NotNull Coordinates pos, @NotNull Board board,
                                                  @NotNull int[] xChange, @NotNull int[] yChange) {
        assert xChange.length == yChange.length;
        Set<Move> result = new HashSet<>();
        assert board.getPieceAt(pos) instanceof Piece;
        Piece p = (Piece) board.getPieceAt(pos);
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new CoordinatesImpl(pos.getX() + xChange[i], pos.getY() + yChange[i]);
            while (!c.isOutOfBounds() && board.getPieceAt(c) instanceof EmptyCell) {
                result.add(new RegularMove(pos, c));
                c = new CoordinatesImpl(c.getX() + xChange[i], c.getY() + yChange[i]);
            }
            if (!c.isOutOfBounds() &&
                    board.getPieceAt(c).getColor() == board.getPieceAt(pos).getColor().inverse()) {
                //can eat
                result.add(new RegularMove(pos, c));
            }

            c = pos; //restore
            if (!c.isOutOfBounds()) {
                PieceColor color = board.getPieceAt(c).getColor();
                if (!c.isOutOfBounds() && color == p.getColor().inverse()) {
                    result.add(new RegularMove(pos, c));
                }
            }
        }
        return result;
    }

    @NotNull
    private static Set<Move> getKingMoves(@NotNull Coordinates kingPos, @NotNull Board board) {
        int[] xChange = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] yChange = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        Set<Move> result = new HashSet<>();
        assert xChange.length == yChange.length;
        assert board.getPieceAt(kingPos) instanceof Piece;
        PieceColor kingColor = board.getPieceAt(kingPos).getColor();
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new CoordinatesImpl(kingPos.getX() + xChange[i], kingPos.getY() + yChange[i]);
            if (!c.isOutOfBounds()) {
                boolean correctColor = board.getPieceAt(c).getColor() != kingColor;
                if (correctColor && !kingAround(c, board, kingColor.inverse())) {
                    result.add(new RegularMove(kingPos, c)); //all the situation when king is attacked will be filtered out later
                }
            }
        }
        if (board.pieceHasNotMovedSinceStartOfGame(kingPos) && !kingIsAttacked(kingColor, board, false)) {
            //check for castling
            List<Coordinates> rooks = findPiecesByTypeAndColor(PieceType.Rook, kingColor, board);
            for (Coordinates rookPos : rooks) {
                if (board.pieceHasNotMovedSinceStartOfGame(rookPos)) {
                    assert rookPos.getY() == kingPos.getY();
                    int direction;
                    if (rookPos.getX() > kingPos.getX()) {
                        direction = 1;
                    } else {
                        assert rookPos.getX() < kingPos.getX();
                        direction = -1;
                    }

                    boolean canCastle = true;
                    Coordinates castleCells = kingPos;
                    while (!castleCells.equals(rookPos) && canCastle) {
                        ChessElement piece = board.getPieceAt(castleCells);
                        if (!(piece instanceof EmptyCell) && piece != board.getPieceAt(kingPos)) {
                            canCastle = false;
                        }
                        castleCells = new CoordinatesImpl(castleCells.getX() + direction, castleCells.getY());
                    }
                    if (canCastle) {
                        Coordinates kingNewPos = new CoordinatesImpl(kingPos.getX() + direction * 2, rookPos.getY());
                        Coordinates rookNewPos = new CoordinatesImpl(kingPos.getX() + direction, rookPos.getY());
                        Castling move = new Castling(kingPos, kingNewPos, rookPos, rookNewPos);
                        result.add(move);
                    }
                }
            }
        }
        return result;
    }

    private static boolean kingAround(@NotNull Coordinates pos, @NotNull Board board, PieceColor opposingKingColor) {
        int[] xChange = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
        int[] yChange = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
        assert xChange.length == yChange.length;
        for (int i = 0; i < xChange.length; i++) {
            Coordinates c = new CoordinatesImpl(pos.getX() + xChange[i], pos.getY() + yChange[i]);
            if (!c.isOutOfBounds()) {
                ChessElement elem = board.getPieceAt(c);
                if (elem.getType() == PieceType.King && elem.getColor() == opposingKingColor) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean kingIsAttacked(@NotNull PieceColor kingColor, @NotNull Board board, boolean checkKing) {
        List<Coordinates> allOpponentPieces = getAllPieces(kingColor.inverse(), board);
        Coordinates kingPos = findKing(kingColor, board);

        for (Coordinates pos : allOpponentPieces) {
            if (checkKing || board.getPieceAt(pos).getType() != PieceType.King) {
                Set<Move> availableMovesForPiece = getAllMovesForPiece(pos, board);
                for (Move m : availableMovesForPiece) {
                    for (Coordinates c : m.getNewPositions()) {
                        if (c.equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean kingIsAttacked(@NotNull PieceColor kingColor, @NotNull Board board) {
        return kingIsAttacked(kingColor, board, true);
    }

    private static boolean kingIsAttackedAfterMove(@NotNull PieceColor color, @NotNull Board b,
                                                   @NotNull Move m) {
        if (m instanceof Castling) {
            Castling castling = (Castling) m;
            Coordinates kingInitialPosition = castling.getKingInitialPosition();
            Coordinates rookInitialPosition = castling.getRookInitialPosition();
            int dir;
            if (kingInitialPosition.getX() < rookInitialPosition.getX()) {
                dir = 1;
            } else {
                assert kingInitialPosition.getX() > rookInitialPosition.getX();
                dir = -1;
            }
            assert kingInitialPosition.getY() == rookInitialPosition.getY();
            for (Coordinates c = kingInitialPosition; c.getX() != rookInitialPosition.getX(); c = new CoordinatesImpl(c.getX() + dir, c.getY())) {
                if (ChessBoardUtil.makeMoveAndEvaluate(b, m, board -> kingIsAttacked(color, board))) {
                    return true;
                }
            }
            return false;
        } else {
            return ChessBoardUtil.makeMoveAndEvaluate(b, m, board -> kingIsAttacked(color, board));
        }
    }

    @NotNull
    private static Coordinates findKing(@NotNull PieceColor kingColor, @NotNull Board board) {
        List<Coordinates> kingLocation = findPiecesByTypeAndColor(PieceType.King, kingColor, board);
        assert kingLocation.size() == 1 : "Wrong number of kings on the board: " + kingLocation;
        return kingLocation.get(0);
    }

    @NotNull
    public static List<Coordinates> findPiecesByTypeAndColor(@NotNull PieceType type, @NotNull PieceColor color,
                                                             @NotNull Board board) {
        ChessElement elemToFind = Piece.createPiece(color, type);
        List<Coordinates> result = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Coordinates coordinates = new CoordinatesImpl(i, j);
                if (board.getPieceAt(coordinates).equals(elemToFind)) {
                    result.add(coordinates);
                }
            }
        }
        return result;
    }

    public static <T> List<T> copy(List<T> toCopy) {
        List<T> res = new ArrayList<>(toCopy.size());
        res.addAll(toCopy);
        return res;
    }

    public static void println(Object s) {
        if (GameRunnerImpl.DEBUG) {
            System.out.println(s);
        }
    }
}
