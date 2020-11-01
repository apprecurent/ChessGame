package com.company.assets;

import com.company.assets.game.Game;
import com.company.assets.gui.Board;

import java.util.ArrayList;
import java.util.List;

public class Row extends Field {
    public Row(Game game, int id) {
        super(game, id);
    }

    @Override
    public List<Square> getSquares() {
        List<Square> squares = new ArrayList<>();
        for (Square square : getGame().getSquares()) {
            if (square.getRow().getId() == getId()) squares.add(square);
        }
        return squares;
    }
}
