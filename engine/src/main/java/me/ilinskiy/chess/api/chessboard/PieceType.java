package me.ilinskiy.chess.api.chessboard;

import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public enum PieceType {
    Pawn, Knight, Bishop, Rook, Queen, King, Empty;

    @NotNull
    public String symbol() {
        return switch (this) {
            case Pawn, Empty -> "";
            case Knight -> "N";
            case Bishop -> "B";
            case Rook -> "R";
            case Queen -> "Q";
            case King -> "K";
        };
    }
}
