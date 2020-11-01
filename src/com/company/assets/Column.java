package com.company.assets;

import com.company.assets.game.Game;
import com.company.assets.gui.Board;

import java.util.ArrayList;
import java.util.List;

public class Column extends Field {
    public Column(Game game, int id) {
        super(game, id);
    }

    public List<Square> getSquares() {
        List<Square> squares = new ArrayList<>();
        for (Square square : getGame().getSquares()) {
            if (square.getColumn().getId() == getId()) squares.add(square);
        }
        return squares;
    }
}
