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
        for (Square square : getGame().getSquares()) {
            if (getId() < 15) {
                if (square.getColumn().getId() + square.getRow().getId() == getId()) {
                    squares.add(square);
                }
            } else {
                if (22+square.getRow().getId()-square.getColumn().getId() == getId()) {
                    squares.add(square);
                }
            }
        }
        return squares;
    }
}
