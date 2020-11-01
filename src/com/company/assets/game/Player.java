package com.company.assets.game;

import com.company.assets.ChessColor;

public class Player {

    private Game game;
    private ChessColor color;

    public Player(ChessColor color, int timer) {
        this.color = color;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    public ChessColor getColor() {
        return this.color;
    }

}
