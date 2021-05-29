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
        // Loopa igenom alla rutor
        for (Square square : getGame().getSquares()) {
            // Lägg till rutan om den har rätt id
            if (square.getRow().getId() == getId()) squares.add(square);
        }
        return squares;
    }
}
