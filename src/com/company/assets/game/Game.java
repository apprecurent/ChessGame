package com.company.assets.game;

import com.company.assets.*;
import com.company.assets.exceptions.PlayerColorException;
import com.company.assets.gui.Board;
import com.company.assets.pieces.*;

import java.awt.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.*;
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
        // Initiera alla listor
        init();

        // Skapa 8 kolumner och rader
        for (int i = 0; i < 8; i++) {
            columns.add(new Column(this, i));
            rows.add(new Row(this, i));
        }

        // Skapa 30 diagonaler
        for (int i = 0; i < 30; i++) {
            diagonals.add(new Diagonal(this, i));
        }

        // Skapa 8*8=64 rutor
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // ange korrekt kolumn, rad, färg samt plats på brädet
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

    private Position position;

    public void makeMove() {
        List<Move> moves = new ArrayList<>();
        for (Piece piece : getBlackPieces()) {
            for (Square square : piece.getAccessibleSquares()) {
                moves.add(new Move(piece, square));
            }
        }

        Random random = new Random();

        int rand = random.nextInt(moves.size());

        simMove(moves.get(rand));
    }

    private int score = 0;
    private int iteration = 0;
    private Map<Move, Integer> moveScore;

    public void simMove(Move move) {
        board.cleanup();
        move.getPiece().getSquare().removePiece();
        move.getPiece().move(move.getSquare());
    }

    public int evaluate(Move move) {
        moveScore = new HashMap<>();
        while (iteration != 3) {

        }

        moveScore.put(move, score);

        iteration = 0;

        return score;
    }

    public void setSavedPosition(Position position) {
        this.position = position;
    }

    public void setPosition(Position position) {
        for (Piece piece : position.getWhitePieces()) {
            piece.setSquare(piece.getSquare());
        }

        for (Piece piece : position.getBlackPieces()) {
            piece.setSquare(piece.getSquare());
        }
    }

    private void startPositions() {


        // Skapa alla svarta pjäser
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

        // Skapa alla vita pjäser
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

        // Sätt samtliga pjäser på rätt ruta
        for (int i = 0; i < 16; i++) {
            squares.get(i).setPiece(blackPieces.get(i));
        }
        for (int i = 0; i < 16; i++) {
            squares.get(i+48).setPiece(whitePieces.get(i));
        }


        /*
        whitePieces.add(new Queen(getWhitePlayer()));

        squares.get(53).setPiece(whitePieces.get(0));

        blackPieces.add(new King(getBlackPlayer()));

        squares.get(50).setPiece(blackPieces.get(0));

        whitePieces.add(new Queen(getWhitePlayer()));

        squares.get(37).setPiece(whitePieces.get(1));

        whitePieces.add(new King(getWhitePlayer()));

        squares.get(34).setPiece(whitePieces.get(2));

        whitePieces.add(new Queen(getWhitePlayer()));

        squares.get(21).setPiece(whitePieces.get(3));

        blackPieces.add(new Queen(getBlackPlayer()));

        squares.get(18).setPiece(blackPieces.get(1));
         */
    }


    // Returnera kungen av en viss färg
    public King getKing(ChessColor color) {
        // Loopa igenom alla pjäser av den färgen och se ifall det är en kung
        for (Piece piece : getPieces(color)) {
            if (piece instanceof King) return (King) piece;
        }
        return null;
    }

    // Returnera på en viss rad och kolumn
    public Square getSquare(int column, int row) {
        // Loopa igenom alla rutor
        for (Square square : squares) {
            // Kolla om kolumn-id och rad-id stämmer överens
            if (square.getColumn().getId() == column && square.getRow().getId() == row) return square;
        }
        return null;
    }

    // Returnera alla pjäser av en viss färg
    public List<Piece> getPieces(ChessColor color) {
        if (color == ChessColor.WHITE) return whitePieces;
        else return blackPieces;
    }

    // Returnera diagonalen till en viss column och rad (ruta)
    public List<Diagonal> getDiagonals(Column column, Row row) {
        List<Diagonal> diagonals = new ArrayList<>();
        // Första diagonalen får man genom summan av rad och kolumn
        diagonals.add(this.diagonals.get(column.getId() + row.getId()));
        // Andra diagonalen får man genom att ta differensen av rad-id och kolumn-id och addera 22
        diagonals.add(this.diagonals.get(22+row.getId()-column.getId()));
        return diagonals;
    }

    public List<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    public List<Piece> getWhitePieces() {
        return this.whitePieces;
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

    public void checkmate() {
        System.out.println("Checkmate");
    }

}
