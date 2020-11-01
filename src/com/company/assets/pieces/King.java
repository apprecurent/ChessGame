package com.company.assets.pieces;

import com.company.assets.game.Player;
import com.company.assets.gui.Board;
import com.company.assets.ChessColor;
import com.company.assets.Square;

import java.util.*;

public class King extends Piece {

    private List<Piece> checkList;

    public King(Player player) {
        super(player);
        checkList = new ArrayList<>();
    }

    public List<Square> getAccessibleSquares() {
        List<Square> squares = new ArrayList<>(getInfluencedSquares());
        squares.removeIf(square -> square.hasPiece() && square.getPiece().getColor() == this.getColor());

        Set<Square> unavailableSquares = new HashSet<>();
        List<Piece> pieces = this.getColor() == ChessColor.WHITE ? getGame().getBlackPieces() : getGame().getWhitePieces();
        for (Piece piece : pieces) unavailableSquares.addAll(piece.getInfluencedSquares());

        // First move
        if (this.getMoves() == 0 && !isChecked()) {
            for (Piece piece : getGame().getPieces(this.getColor())) {
                if (piece instanceof Rook && piece.getInfluencedSquares().contains(this.getSquare()) && piece.getMoves() == 0) {
                    int columnModifier = piece.getSquare().getColumn().getId() < this.getSquare().getColumn().getId() ? -2 : 2;
                    if (!unavailableSquares.contains(getGame().getSquare(this.getSquare().getColumn().getId() + (columnModifier - columnModifier/2), this.getSquare().getRow().getId())))
                        squares.add(getGame().getSquare(this.getSquare().getColumn().getId() + columnModifier, this.getSquare().getRow().getId()));
                }
            }
        }

        squares.removeAll(unavailableSquares);

        return squares;
    }

    @Override
    public List<Square> getInfluencedSquares() {
        List<Square> squares = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Skip the current square
                if (i == 0 && j == 0) continue;
                Square square = getGame().getSquare(getSquare().getColumn().getId() + i, getSquare().getRow().getId() + j);
                if (square != null) squares.add(square);
            }
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

    public boolean isChecked() {
        return !this.checkList.isEmpty();
    }

    public void addCheck(Piece piece) {
        checkList.add(piece);
        getSquare().setChecked(true);
    }

    public void removeChecks() {
        checkList.clear();
        getSquare().setChecked(false);
    }

    public List<Piece> getChecks() {
        return checkList;
    }

    public String getPath() {
        String color = getColor() == ChessColor.WHITE ? "white" : "black";
        return "C:\\Users\\ville\\Downloads\\Chess\\src\\com\\company\\resources\\" + color + "_king.png";
    }

    // Find all the squares between the piece that puts this King in check and the King.
    public List<Square> getBlockableSquares() {
        // Not in check
        List<Square> squares = new ArrayList<>();
        for (Piece piece : checkList) {
            squares.add(piece.getSquare());
            /*
            For each piece you want to find all the possible squares that could be used to block the current check
            If the check is done by more than one piece, the only accessible squares are the ones that those pieces have in common
            If a knight or pawn is checking, they check can only be blocked if this is the only piece that is checking.
             */

            // Column
            if (piece.getInfluencedColumnSquares().contains(this.getSquare())) {
                if (piece.getSquare().getRow().getId() < this.getSquare().getRow().getId()) {
                    squares.addAll(piece.getSquare().getColumn().getSquares().subList(this.getSquare().getColumn().getSquares().indexOf(piece.getSquare()), this.getSquare().getColumn().getSquares().indexOf(this.getSquare())));
                } else {
                    squares.addAll(piece.getSquare().getColumn().getSquares().subList(this.getSquare().getColumn().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getColumn().getSquares().indexOf(piece.getSquare())));
                }
                continue;
            }

            // Row
            else if (piece.getInfluencedRowSquares().contains(this.getSquare())) {
                if (piece.getSquare().getColumn().getId() < this.getSquare().getColumn().getId()) {
                    // The influenced squares does not contain the current square of the piece, making it not suitable
                    squares.addAll(piece.getSquare().getRow().getSquares().subList(this.getSquare().getRow().getSquares().indexOf(piece.getSquare()), this.getSquare().getRow().getSquares().indexOf(this.getSquare())));
                } else {
                    squares.addAll(piece.getSquare().getRow().getSquares().subList(this.getSquare().getRow().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getRow().getSquares().indexOf(piece.getSquare())));
                }
                continue;
            }
            else if (piece.getInfluencedFirstDiagonalSquares().contains(this.getSquare())) {
                if (piece.getSquare().getColumn().getId() < this.getSquare().getColumn().getId()) {
                    squares.addAll(piece.getSquare().getFirstDiagonal().getSquares().subList(this.getSquare().getFirstDiagonal().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getFirstDiagonal().getSquares().indexOf(piece.getSquare())));
                } else {
                    squares.addAll(piece.getSquare().getFirstDiagonal().getSquares().subList(this.getSquare().getFirstDiagonal().getSquares().indexOf(piece.getSquare()), this.getSquare().getFirstDiagonal().getSquares().indexOf(this.getSquare())));
                }
                continue;
            }
            else if (piece.getInfluencedSecondDiagonalSquares().contains(this.getSquare())) {
                if (piece.getSquare().getColumn().getId() < this.getSquare().getColumn().getId()) {
                    squares.addAll(piece.getSquare().getSecondDiagonal().getSquares().subList(this.getSquare().getSecondDiagonal().getSquares().indexOf(piece.getSquare()), this.getSquare().getSecondDiagonal().getSquares().indexOf(this.getSquare())));
                } else {
                    squares.addAll(piece.getSquare().getSecondDiagonal().getSquares().subList(this.getSquare().getSecondDiagonal().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getSecondDiagonal().getSquares().indexOf(piece.getSquare())));
                }
                continue;
            }
            // Pawn or knight
            squares.add(piece.getSquare());
        }

        if (checkList.size() > 1) {
            Set<Square> squareSet = new LinkedHashSet<>(squares);

            int difference = squares.size() - squareSet.size();

            if (difference == 0) squares.clear();
            else if (difference > 0) squares.removeAll(squareSet);
        }

        return squares;
    }

}
