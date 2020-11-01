package com.company.assets.pieces;

import com.company.assets.game.Player;
import com.company.assets.gui.Board;
import com.company.assets.ChessColor;
import com.company.assets.Square;
import com.company.utils.Util;

import java.util.*;

public class Rook extends Piece {
    public Rook(Player player) {
        super(player);
    }

    @Override
    public List<Square> getAccessibleSquares() {

        List<Square> squares = new ArrayList<>(getInfluencedSquares());
        squares.removeIf(square -> square.hasPiece() && square.getPiece().getColor() == this.getColor());
        King king = getGame().getKing(this.getColor());
        List<Square> checkSquares = new ArrayList<>();
        ChessColor otherColor = Util.getOtherColor(this.getColor());
        if (king.isChecked()) {
            for (Square square : getGame().getKing(this.getColor()).getBlockableSquares()) {
                if (squares.contains(square)) checkSquares.add(square);
            }
            squares.clear();
            squares.addAll(checkSquares);
        } else if (this.getSquares().contains(king.getSquare())){
            // Remove all squares that can not be accessed due to the king being in check
            for (Piece piece : getGame().getPieces(otherColor)) {
                if (this.getRowSquares().contains(king.getSquare()) && piece.getInfluencedRowSquares().contains(this.getSquare()))
                    squares.removeIf(square -> !this.getInfluencedRowSquares().contains(square));
                else if (this.getColumnSquares().contains(king.getSquare()) && piece.getInfluencedColumnSquares().contains(this.getSquare()))
                    squares.removeIf(square -> !this.getInfluencedColumnSquares().contains(square));
                else if (this.getFirstDiagonalSquares().contains(king.getSquare()) && piece.getInfluencedFirstDiagonalSquares().contains(this.getSquare()))
                    squares.removeIf(square -> !this.getInfluencedFirstDiagonalSquares().contains(square));
                else if (this.getSecondDiagonalSquares().contains(king.getSquare()) && piece.getInfluencedSecondDiagonalSquares().contains(this.getSquare()))
                    squares.removeIf(square -> !this.getInfluencedSecondDiagonalSquares().contains(square));
            }
        }


        return squares;

    }

    @Override
    public List<Square> getInfluencedSquares() {
        Set<Square> squares = new LinkedHashSet<>();
        squares.addAll(this.getInfluencedRowSquares());
        squares.addAll(this.getInfluencedColumnSquares());

        return new ArrayList<>(squares);
    }

    @Override
    public List<Square> getInfluencedRowSquares() {
        return new ArrayList<>(this.getRowSquares());
    }

    @Override
    public List<Square> getInfluencedColumnSquares() {
        return new ArrayList<>(this.getColumnSquares());
    }

    @Override
    public List<Square> getInfluencedFirstDiagonalSquares() {
        return new ArrayList<>();
    }

    @Override
    public List<Square> getInfluencedSecondDiagonalSquares() {
        return new ArrayList<>();
    }

    @Override
    public String getPath() {
        String color = getColor() == ChessColor.WHITE ? "white" : "black";
        return "C:\\Users\\ville\\Downloads\\Chess\\src\\com\\company\\resources\\" + color + "_rook.png";
    }
}
