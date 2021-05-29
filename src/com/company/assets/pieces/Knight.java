package com.company.assets.pieces;

import com.company.assets.game.Player;
import com.company.assets.gui.Board;
import com.company.assets.ChessColor;
import com.company.assets.Square;
import com.company.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Player player) {
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
        List<Square> squares = new ArrayList<>();
        // loopar medan i är mindre än 2 (0 eller 1)
        for (int i = 0; i < 2; i++) {

            // Sätt värde till positivt (1) ifall det är första iterationen eller negativt (-1) ifall det är andra iterationen
            int value = i == 0 ? 1 : -1;

            // loopar medan j är mindre eller lika med 2 (-2, -1, 0, 1, 2)
            for (int j = -2; j <= 2; j++) {
                // hoppa över ifall j är lika med 0
                if (j == 0) continue;

                // variabeln row säger hur mycket och i vilken riktning den aktuella rutan kommer att förflyttas i förhållande till pjäsen
                // för första iterationen får vi (1 * absolutvärde(-2%2) + 1) --> 1 * (0 + 1) --> 1
                int row = value * (Math.abs((j%2)) + 1);

                // initiera rutan med den aktuella rutan första iterationen ger rutan placerad
                // två steg vänster i x-led (j = -2) samt ett steg uppåt i y-led (row = 1)
                Square square = getGame().getSquare(getSquare().getColumn().getId() + j, getSquare().getRow().getId() + row);

                // se till att rutan existerar (inte utanför brädet ifall hästen t.ex står på A1)
                if (square != null) squares.add(square);
            }
        }

        return squares;
    }

    // Returnera tomma
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
        return "C:\\Users\\ville\\Downloads\\Chess\\src\\com\\company\\resources\\" + color + "_knight.png";
    }
}
