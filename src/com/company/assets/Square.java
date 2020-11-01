package com.company.assets;

import com.company.assets.game.Game;
import com.company.assets.gui.Board;
import com.company.assets.pieces.Piece;

import java.util.List;
import java.awt.*;

public class Square {

    private Game game;
    private Row row;
    private Column column;
    private ChessColor color;
    private Point point;
    private Piece piece;

    private boolean selectable, active, capturable, checked, marked;

    public Square(Game game, Column column, Row row, ChessColor color, Point point) {
        this.game = game;
        this.row = row;
        this.column = column;
        this.color = color;
        this.point = point;
        this.selectable = false;
    }

    public Column getColumn() {
        return column;
    }

    public Row getRow() {
        return row;
    }

    public List<Diagonal> getDiagonals() {
        return game.getDiagonals(column, row);
    }

    public Color getColor() {
        Color rgbColor = new Color(187, 185, 255);
        switch (this.color) {
            case BLACK:
                rgbColor = new Color(112, 82, 0);
                break;
            case WHITE:
                rgbColor = new Color(255, 232, 192, 255);
                break;
        }
        return rgbColor;
    }

    public void paint(Graphics g) {
        Color color = getColor();
        if (selectable) color = new Color(95, 175, 220);
        if (capturable) color = new Color(220, 75, 45);
        if (checked) color = new Color(150, 35, 10);
        if (active) color = new Color(200, 150, 15);


        g.setColor(color);
        g.fillRect(point.x, point.y, 100, 100);

        if (marked) {
            g.setColor(Color.BLACK);
            g.fillRect(point.x  , point.y, 1, 100);
            g.fillRect(point.x, point.y + 100-1, 100, 1);
            g.fillRect(point.x +100-1, point.y, 1, 100);
            g.fillRect(point.x , point.y, 100, 1);
        }
    }
    
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCapturable(boolean capturable) {
        this.capturable = capturable;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

        piece.setSquare(this);
    }

    public Diagonal getFirstDiagonal() {
        return game.getDiagonals(column, row).get(0);
    }

    public Diagonal getSecondDiagonal() {
        return game.getDiagonals(column, row).get(1);
    }

    public Point getPosition() {
        return this.point;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public void removePiece() {
        this.piece = null;
    }

    public boolean isHit(Point point) {
        return this.point.x < point.x && this.point.y < point.y && this.point.x + 100 > point.x && this.point.y + 100 > point.y;
    }

    @Override
    public String toString() {
        return "Square{" +
                "row=" + row.getId() +
                ", column=" + column.getId() +
                ", color=" + color +
                ", point=" + point +
                ", piece=" + piece +
                '}';
    }
}
