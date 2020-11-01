package com.company.assets.game;

import com.company.assets.*;
import com.company.assets.exceptions.PlayerColorException;
import com.company.assets.gui.Board;
import com.company.assets.pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board;
    private Player player1, player2;

    private java.util.List<Square> squares;
    private java.util.List<Column> columns;
    private java.util.List<Row> rows;
    private java.util.List<Diagonal> diagonals;

    private java.util.List<Piece> blackPieces;
    private List<Piece> whitePieces;

    private int turn;

    private boolean exit;

    private Game() {
        init();

        for (int i = 0; i < 8; i++) {
            columns.add(new Column(this, i));
            rows.add(new Row(this, i));
        }

        for (int i = 0; i < 30; i++) {
            diagonals.add(new Diagonal(this, i));
        }

        for (int i = 0; i < 8; i++) {
            // Make row 1 be first one from bottom
            for (int j = 0; j < 8; j++) {
                squares.add(new Square(this, columns.get(j), rows.get(i), ChessColor.values()[(j+i%2)% 2], new Point(j*100, i*100)));
            }
        }
    }

    public Game(Board board, Player player1, Player player2) throws PlayerColorException{
        this();
        this.board = board;

        if (player1.getColor() == player2.getColor()) throw new PlayerColorException();

        this.player1 = player1;
        this.player2 = player2;

        player1.setGame(this);
        player2.setGame(this);

        board.repaint();

        startPositions();
    }

    public Board getBoard() {
        return this.board;
    }
    
    public Player getWhitePlayer() {
        return player1.getColor() == ChessColor.WHITE ? player1 : player2;
    }

    public Player getBlackPlayer() {
        return player2.getColor() == ChessColor.BLACK ? player2 : player1;
    }

    public List<Square> getSquares() {
        return this.squares;
    }

    private void init() {
        squares = new ArrayList<>();
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        diagonals = new ArrayList<>();

        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
    }

    private void startPositions() {
        blackPieces.add(new Rook(getBlackPlayer()));
        blackPieces.add(new Knight(getBlackPlayer()));
        blackPieces.add(new Bishop(getBlackPlayer()));
        blackPieces.add(new Queen(getBlackPlayer()));
        blackPieces.add(new King(getBlackPlayer()));
        blackPieces.add(new Bishop(getBlackPlayer()));
        blackPieces.add(new Knight(getBlackPlayer()));
        blackPieces.add(new Rook(getBlackPlayer()));
        for (int i = 0 ; i < 8; i++) {
            blackPieces.add(new Pawn(getBlackPlayer()));
        }
        for (int i = 0 ; i < 8; i++) {
            whitePieces.add(new Pawn(getWhitePlayer()));
        }
        whitePieces.add(new Rook(getWhitePlayer()));
        whitePieces.add(new Knight(getWhitePlayer()));
        whitePieces.add(new Bishop(getWhitePlayer()));
        whitePieces.add(new Queen(getWhitePlayer()));
        whitePieces.add(new King(getWhitePlayer()));
        whitePieces.add(new Bishop(getWhitePlayer()));
        whitePieces.add(new Knight(getWhitePlayer()));
        whitePieces.add(new Rook(getWhitePlayer()));

        for (int i = 0; i < 16; i++) {
            squares.get(i).setPiece(blackPieces.get(i));
        }
        for (int i = 0; i < 16; i++) {
            squares.get(i+48).setPiece(whitePieces.get(i));
        }
    }


    public King getKing(ChessColor color) {
        for (Piece piece : getPieces(color)) {
            if (piece instanceof King) return (King) piece;
        }
        return null;
    }

    public List<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    public List<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Square getSquare(int column, int row) {
        for (Square square : squares) {
            if (square.getColumn().getId() == column && square.getRow().getId() == row) return square;
        }
        return null;
    }

    public Column getColumn(int id) {
        for (Column column : columns) {
            if (column.getId() == id) return column;
        }
        return null;
    }
    public Row getRow(int id) {
        for (Row row : rows) {
            if (row.getId() == id) return row;
        }
        return null;
    }

    public void deletePiece(Piece piece) {
        if (whitePieces.contains(piece)) whitePieces.remove(piece);
        else blackPieces.remove(piece);
    }

    public List<Piece> getPieces(ChessColor color) {
        if (color == ChessColor.WHITE) return whitePieces;
        else return blackPieces;
    }

    public void checkmate() {
        System.out.println("Checkmate");
        /*
        setStartPositions();
        turn = 0;
         */
    }

    public void pawnPromotion() {

    }

    public List<Diagonal> getDiagonals(Column column, Row row) {
        List<Diagonal> diagonals = new ArrayList<>();
        diagonals.add(this.diagonals.get(column.getId() + row.getId()));
        diagonals.add(this.diagonals.get(22+row.getId()-column.getId()));
        return diagonals;
    }

}
