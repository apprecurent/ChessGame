package com.company.assets.pieces;

import com.company.assets.game.Player;
import com.company.assets.gui.Board;
import com.company.assets.ChessColor;
import com.company.assets.Square;
import com.company.utils.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Pawn extends Piece {

    private boolean enPassant;
    private Set<Square> enPassantSquares;

    public Pawn(Player player) {
        super(player);

        enPassantSquares = new HashSet<>();
    }

    @Override
    public List<Square> getAccessibleSquares() {
        List<Square> squares = new ArrayList<>();
        int value = 0;
        switch (getColor()) {
            case WHITE:
                value = -1;
                if (this.getSquare().getRow().getId() == 6 && !this.getSquare().getColumn().getSquares().get(4).hasPiece() && !this.getSquare().getColumn().getSquares().get(5).hasPiece())
                    squares.add(this.getSquare().getColumn().getSquares().get(4));

                break;
            case BLACK:
                value = 1;
                if (this.getSquare().getRow().getId() == 1 && !this.getSquare().getColumn().getSquares().get(3).hasPiece() && !this.getSquare().getColumn().getSquares().get(2).hasPiece())
                    squares.add(this.getSquare().getColumn().getSquares().get(3));
                break;
        }

        // Square infront
        if (value == -1 && getSquare().getRow().getId() == 0) {
            getSquare().getPiece().getImageHolder().setVisible(false);
            getSquare().removePiece();
            getGame().deletePiece(this);
            Piece newPiece = new Queen(getGame().getWhitePlayer());
            getGame().getWhitePieces().add(newPiece);
            getSquare().setPiece(newPiece);
            return new ArrayList<>();

        } else if (value == 1 && getSquare().getRow().getId() == 7) {
            getSquare().getPiece().getImageHolder().setVisible(false);
            getSquare().removePiece();
            getGame().deletePiece(this);
            Piece newPiece = new Queen(getGame().getWhitePlayer());
            getGame().getWhitePieces().add(newPiece);
            getSquare().setPiece(newPiece);
            return new ArrayList<>();
        }
        Square square = getSquare().getColumn().getSquares().get(getSquare().getRow().getId() + value);
        if (!square.hasPiece()) {
            squares.add(square);
        }

        // Piece diagonal
        for (int j = 0; j < 2; j++) {
            Square diagonalSquare;
            try {
                diagonalSquare = getSquare().getDiagonals().get(j).getSquares().get(getSquare().getDiagonals().get(j).getSquares().indexOf(getSquare()) + value);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            Square horizontalSquare = getGame().getSquare(diagonalSquare.getColumn().getId(), diagonalSquare.getRow().getId() - value);
            /*
            System.out.println("Dia: " + diagonalSquare);
            System.out.println("Hor: " + horizontalSquare);
             */
            if (diagonalSquare.hasPiece() && diagonalSquare.getPiece().getColor() != this.getColor()) squares.add(diagonalSquare);
            else if (horizontalSquare.hasPiece() && horizontalSquare.getPiece() instanceof Pawn && ((Pawn) horizontalSquare.getPiece()).hasEnPassant()) {
                squares.add(diagonalSquare);
                enPassantSquares.add(diagonalSquare);
            }
        }

        King king = getGame().getKing(this.getColor());
        List<Square> checkSquares = new ArrayList<>();
        ChessColor otherColor = Util.getOtherColor(this.getColor());
        if (king.isChecked()) {
            for (Square s : getGame().getKing(this.getColor()).getBlockableSquares()) {
                if (squares.contains(s)) checkSquares.add(s);
            }
            squares.clear();
            squares.addAll(checkSquares);
        } else if (this.getSquares().contains(king.getSquare())){
            // Remove all squares that can not be accessed due to the king being in check
            for (Piece piece : getGame().getPieces(otherColor)) {
                if (this.getRowSquares().contains(king.getSquare()) && piece.getInfluencedRowSquares().contains(this.getSquare()))
                    squares.removeIf(s -> !this.getInfluencedRowSquares().contains(s));
                else if (this.getColumnSquares().contains(king.getSquare()) && piece.getInfluencedColumnSquares().contains(this.getSquare()))
                    squares.removeIf(s -> s.getColumn() != this.getSquare().getColumn());
                else if (this.getFirstDiagonalSquares().contains(king.getSquare()) && piece.getInfluencedFirstDiagonalSquares().contains(this.getSquare()))
                    squares.removeIf(s -> !this.getInfluencedFirstDiagonalSquares().contains(s));
                else if (this.getSecondDiagonalSquares().contains(king.getSquare()) && piece.getInfluencedSecondDiagonalSquares().contains(this.getSquare()))
                    squares.removeIf(s -> !this.getInfluencedSecondDiagonalSquares().contains(s));
            }
        }
        return squares;
    }

    @Override
    public List<Square> getInfluencedSquares() {
        List<Square> squares = new ArrayList<>();
        int value = 0;
        switch (getColor()) {
            case WHITE:
                value = -1;
                break;
            case BLACK:
                value = 1;
                break;
        }
        for (int j = 0; j < 2; j++) {
            Square diagonalSquare;
            try {
                diagonalSquare = getSquare().getDiagonals().get(j).getSquares().get(getSquare().getDiagonals().get(j).getSquares().indexOf(getSquare()) + value);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            squares.add(diagonalSquare);
        }
        return squares;
    }

    @Override
    public List<Square> getInfluencedRowSquares() {
        return new ArrayList<>();
    }

    @Override
    public List<Square> getInfluencedColumnSquares() {
        return new ArrayList<>();
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
        return "C:\\Users\\ville\\Downloads\\Chess\\src\\com\\company\\resources\\" + color + "_pawn.png";
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public boolean hasEnPassant() {
        return this.enPassant;
    }

    public Set<Square> getEnPassantSquares() {
        return this.enPassantSquares;
    }

}
