package com.company.assets.game;

import com.company.assets.Square;
import com.company.assets.pieces.Piece;

public class Move {

    private Piece piece;
    private Square square;

    public Move(Piece piece, Square square) {
        this.piece = piece;
        this.square = square;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getSquare() {
        return square;
    }


}
