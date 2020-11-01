package com.company.utils;

import com.company.assets.ChessColor;

public class Util {

    public static ChessColor getOtherColor(ChessColor color) {
        return color == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;
    }

}
