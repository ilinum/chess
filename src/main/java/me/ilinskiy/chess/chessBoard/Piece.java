package me.ilinskiy.chess.chessBoard;


import me.ilinskiy.chess.annotations.NotNull;

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
        for (PieceType pt : ChessBoardUtil.backRowPieceTypes) {
            whitePieces.put(pt, new Piece(PieceColor.White, pt));
            blackPieces.put(pt, new Piece(PieceColor.Black, pt));
        }
        blackPieces.put(PieceType.Pawn, new Piece(PieceColor.Black, PieceType.Pawn));
        whitePieces.put(PieceType.Pawn, new Piece(PieceColor.White, PieceType.Pawn));
    }

    @NotNull
    public static Piece createPiece(@NotNull PieceColor pColor, @NotNull PieceType pType) {
        Piece res = null;
        switch (pColor) {
            case Black:
                res = blackPieces.get(pType);
                break;
            case White:
                res = whitePieces.get(pType);
                break;
            case Empty:
                throw new IllegalArgumentException("Color cannot be empty!");
        }
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
        int colorMultiplier;
        switch (color) {
            case White:
                colorMultiplier = 1;
                break;
            case Black:
                colorMultiplier = -1;
                break;
            case Empty:
                colorMultiplier = 0;
                break;
            default:
                throw new RuntimeException("Unknown color!");
        }
        int pieceTypeMultiplier;
        switch (type) { //could I use Enum.ordinal here?
            case Pawn:
                pieceTypeMultiplier = 1;
                break;
            case Knight:
                pieceTypeMultiplier = 2;
                break;
            case Bishop:
                pieceTypeMultiplier = 3;
                break;
            case Rook:
                pieceTypeMultiplier = 4;
                break;
            case Queen:
                pieceTypeMultiplier = 5;
                break;
            case King:
                pieceTypeMultiplier = 6;
                break;
            case Empty:
                pieceTypeMultiplier = 7;
                break;
            default:
                throw new RuntimeException("Unknown type!");
        }
        return colorMultiplier * pieceTypeMultiplier;
    }
}
