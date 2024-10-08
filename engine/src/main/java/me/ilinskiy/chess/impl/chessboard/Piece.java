package me.ilinskiy.chess.impl.chessboard;


import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class Piece implements ChessElement {
    private final PieceColor color;
    private final PieceType type;

    private static final HashMap<PieceType, Piece> whitePieces;
    private static final HashMap<PieceType, Piece> blackPieces;

    static {
        whitePieces = new HashMap<>();
        blackPieces = new HashMap<>();
        for (PieceType pt : PieceType.values()) {
            if (!pt.equals(PieceType.Empty)) {
                whitePieces.put(pt, new Piece(PieceColor.White, pt));
                blackPieces.put(pt, new Piece(PieceColor.Black, pt));
            }
        }
        blackPieces.put(PieceType.Pawn, new Piece(PieceColor.Black, PieceType.Pawn));
        whitePieces.put(PieceType.Pawn, new Piece(PieceColor.White, PieceType.Pawn));
    }

    @NotNull
    public static Piece createPiece(@NotNull PieceColor pColor, @NotNull PieceType pType) {
        Piece res = switch (pColor) {
            case Black -> blackPieces.get(pType);
            case White -> whitePieces.get(pType);
            case Empty -> throw new IllegalArgumentException("Color cannot be empty!");
        };
        if (res == null) {
            throw new IllegalArgumentException("There's no piece type: " + pType);
        }
        return res;
    }

    private Piece(PieceColor pColor, PieceType pType) {
        color = pColor;
        type = pType;
    }

    @NotNull
    @Override
    public PieceType getType() {
        return type;
    }

    @NotNull
    @Override
    public PieceColor getColor() {
        return color;
    }

    @Override
    @NotNull
    public String toString() {
        return getColor() + " " + getType();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Piece && getColor() == ((Piece) o).getColor() && getType() == ((Piece) o).getType();
    }

    @Override
    public int hashCode() {
        int colorMultiplier = switch (color) {
            case White -> 1;
            case Black -> -1;
            case Empty -> 0;
        };
        int pieceTypeMultiplier = switch (type) { //could I use Enum.ordinal here?
            case Pawn -> 1;
            case Knight -> 2;
            case Bishop -> 3;
            case Rook -> 4;
            case Queen -> 5;
            case King -> 6;
            case Empty -> 7;
        };
        return colorMultiplier * pieceTypeMultiplier;
    }
}
