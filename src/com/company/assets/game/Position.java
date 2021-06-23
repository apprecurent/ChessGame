package com.company.assets.game;

import com.company.assets.pieces.Piece;

import java.util.List;

public class Position {

    private List<Piece> whitePieces, blackPieces;

    public Position(List<Piece> whitePieces, List<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }


}
