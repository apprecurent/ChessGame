package com.company.assets;

import com.company.assets.game.Game;
import com.company.assets.gui.Board;

import java.util.ArrayList;
import java.util.List;

public class Diagonal extends Field{
    public Diagonal(Game game, int id) {
        super(game, id);
    }

    @Override
    public List<Square> getSquares() {
        List<Square> squares = new ArrayList<>();
        // Loopa igenom alla rutor
        for (Square square : getGame().getSquares()) {
            // Alla med id under 15 kommer vara i en viss riktning
            if (getId() < 15) {
                // Första diagonalen får man genom summan av rad och kolumn
                if (square.getColumn().getId() + square.getRow().getId() == getId()) {
                    squares.add(square);
                }
                // Alla med id lika med eller över 15 kommer vara i motsatt riktning
            } else {
                // Andra diagonalen får man genom att ta differensen av rad-id och kolumn-id och addera 22
                // Andledningen till att 22 adderas är för att 22 är mitt emellan 15 och 29 (största och minsta id på dessa diagonaler)
                if (22+square.getRow().getId()-square.getColumn().getId() == getId()) {
                    squares.add(square);
                }
            }
        }
        // Returnera rätt rutor
        return squares;
    }
}
