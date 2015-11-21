package me.ilinskiy.chess.chessBoard;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public enum PieceColor {
    Black, White, Empty;

    public PieceColor inverse() {
        if (this == PieceColor.Empty) {
            throw new IllegalArgumentException("You can't call inverse on Empty!");
        }
        return this == PieceColor.Black ? PieceColor.White : PieceColor.Black;
    }
}
