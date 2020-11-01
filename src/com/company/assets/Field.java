package com.company.assets;

import com.company.assets.game.Game;
import com.company.assets.gui.Board;

import java.util.List;

public abstract class Field {

    public enum FieldConstants {
        COLUMN, ROW, DIAGONAL_LEFT, DIAGONAL_RIGHT
    }

    private Game game;
    private int id;

    public Field(Game game, int id) {
        this.game = game;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Game getGame() {
        return this.game;
    }

    public abstract List<Square> getSquares();
}
