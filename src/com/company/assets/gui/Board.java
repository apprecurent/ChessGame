package com.company.assets.gui;

import com.company.assets.*;
import com.company.assets.exceptions.PlayerColorException;
import com.company.assets.game.Game;
import com.company.assets.game.Player;
import com.company.assets.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Board extends JLayeredPane implements MouseListener, MouseMotionListener {

    private Frame frame;

    private int turn;

    private Game game;

    private boolean exit;

    private Piece selectedPiece = null;
    private Square lastSquare = null;

    public Board(Frame frame) {
        this.frame = frame;

        init();

        try {
            newGame(new Game(this, new Player(ChessColor.WHITE, 10), new Player(ChessColor.BLACK, 10)));
        } catch (PlayerColorException e) {
            e.printStackTrace();
        }
    }

    public void newGame(Game game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (Square square : game.getSquares()) {
            square.paint(g);
        }

    }

    private void init() {
        addMouseListener(this);
        addMouseMotionListener(this);

        turn = 0;
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Square square : game.getSquares()) {
            if (square.isHit(e.getPoint()) && square.hasPiece()) {
                ChessColor color = ChessColor.values()[turn];
                // Not their turn
                if (color != square.getPiece().getColor()) return;

                // Only moves available when the king is in check

                selectedPiece = square.getPiece();

                selectedPiece.givePriority(true);
                square.setActive(true);
                for (Square accessibleSquare : selectedPiece.getAccessibleSquares()) {
                    accessibleSquare.setSelectable(true);
                    accessibleSquare.setMarked(true);
                    accessibleSquare.setCapturable(accessibleSquare.hasPiece());
                }
                square.removePiece();
                lastSquare = square;
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedPiece != null) {
            for (Square square : game.getSquares()) {
                if (square.isHit(e.getPoint())) {
                    if (!selectedPiece.move(square)) lastSquare.setPiece(selectedPiece);
                    cleanup();
                    square.setMarked(true);
                    return;
                }
            }
            lastSquare.setPiece(selectedPiece);
            cleanup();
        }
    }

    public void changeTurn() {
        turn = 1 - turn;
    }

    public void cleanup() {
        lastSquare.setActive(false);
        if (selectedPiece instanceof King) lastSquare.setChecked(((King) selectedPiece).isChecked());
        for (Square accessibleSquare : game.getSquares()) {
            accessibleSquare.setMarked(false);
            accessibleSquare.setSelectable(false);
            accessibleSquare.setCapturable(false);
            accessibleSquare.setChecked(false);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Square square : game.getSquares()) {
            if (square.isHit(e.getPoint())) {
                square.setMarked(true);
            } else {
                square.setMarked(false);
            }

        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        exit = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        exit = true;
        if (selectedPiece == null)
            for (Square square : game.getSquares()) {
                square.setMarked(false);
            }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedPiece != null && !exit) {
            selectedPiece.setPosition(new Point(e.getX() - 50, e.getY() - 50));
        }
    }
}
