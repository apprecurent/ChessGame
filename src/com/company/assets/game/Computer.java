package com.company.assets.game;

import com.company.assets.ChessColor;

public class Computer extends Player {

    private int difficulty;

    public Computer(ChessColor color, int timer, int difficulty) {
        super(color, timer);

        this.difficulty = difficulty;
    }
}
