package com.company.assets.pieces;

import com.company.assets.*;
import com.company.assets.game.Game;
import com.company.assets.game.Player;
import com.company.assets.game.Position;
import com.company.assets.gui.Board;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Piece implements Cloneable {

    private Player player;
    private Square square;
    private JLabel imageHolder;

    private Field checkingField;

    private int moves;

    public Piece(Player player) {
        
        this.player = player;

        imageHolder = new JLabel();
        player.getGame().getBoard().add(imageHolder);
        imageHolder.setSize(100, 100);
        setImage(getPath());

    }

    public Player getPlayer() {
        return this.player;
    }

    public void setImage(String path) {
        ImageIcon defaultIcon = new ImageIcon(path);
        Image image = defaultIcon.getImage().getScaledInstance(imageHolder.getWidth(), imageHolder.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);
        imageHolder.setIcon(icon);
    }

    public boolean move(Square square) {

        if (!getAccessibleSquares().contains(square)) return false;
        else if (square.hasPiece()) {
                square.getPiece().getImageHolder().setVisible(false);
                getGame().deletePiece(square.getPiece());
        }

        for (Piece piece : getGame().getPieces(this.getColor())) {
            if (piece instanceof Pawn) ((Pawn) piece).setEnPassant(false);
        }
        if (this instanceof King) {
            if (square.equals(getGame().getSquare(this.square.getColumn().getId() + 2, this.square.getRow().getId()))) {
                Rook rook = (Rook) getGame().getSquare(this.square.getColumn().getId() + 3, this.square.getRow().getId()).getPiece();
                rook.getSquare().removePiece();
                getGame().getSquare(this.square.getColumn().getId() + 1, this.square.getRow().getId()).setPiece(rook);
            } else if (square.equals(getGame().getSquare(this.square.getColumn().getId() - 2, this.square.getRow().getId()))) {
                Rook rook = (Rook) getGame().getSquare(this.square.getColumn().getId() - 4, this.square.getRow().getId()).getPiece();
                rook.getSquare().removePiece();
                getGame().getSquare(this.square.getColumn().getId() - 1, this.square.getRow().getId()).setPiece(rook);
            }
        }
        else if (this instanceof Pawn) {
            if (square.equals(getGame().getSquare(this.square.getColumn().getId(), this.square.getRow().getId() + 2)) || square.equals(getGame().getSquare(this.square.getColumn().getId(), this.square.getRow().getId() - 2))) ((Pawn) this).setEnPassant(true);

            for (Square s : ((Pawn) this).getEnPassantSquares()) {
                int test = square.getRow().getId() - this.getSquare().getRow().getId();
                if (square.equals(s)) {
                    Square pawnSquare = getGame().getSquare(square.getColumn().getId(), square.getRow().getId() - test);
                    pawnSquare.getPiece().getImageHolder().setVisible(false);
                    getGame().deletePiece(pawnSquare.getPiece());
                    pawnSquare.removePiece();
                }
            }
        }
        getGame().getBoard().changeTurn();
        square.setPiece(this);
        moves++;

        if (this instanceof Pawn && (getSquare().getRow().getId() == 0)) {
            getSquare().getPiece().getImageHolder().setVisible(false);
            getSquare().removePiece();
            getGame().deletePiece(this);
            Piece newPiece = new Queen(getGame().getWhitePlayer());
            getGame().getWhitePieces().add(newPiece);
            getSquare().setPiece(newPiece);

        } else if (this instanceof Pawn && (getSquare().getRow().getId() == 7)) {
            getSquare().getPiece().getImageHolder().setVisible(false);
            getSquare().removePiece();
            getGame().deletePiece(this);
            Piece newPiece = new Queen(getGame().getBlackPlayer());
            getGame().getBlackPieces().add(newPiece);
            getSquare().setPiece(newPiece);

        }

        ChessColor otherColor = this.getColor() == ChessColor.WHITE ? ChessColor.BLACK : ChessColor.WHITE;

        if (getGame().getKing(this.getColor()).isChecked()) getGame().getKing(this.getColor()).removeChecks();
        else {
            for (Piece piece : getGame().getPieces(this.getColor())) {
                if (piece.getAccessibleSquares().contains(getGame().getKing(otherColor).getSquare())) {
                    getGame().getKing(otherColor).addCheck(piece);
                }
            }
        }

        boolean checkmate = true;
        // Fix checkmate, if no moves can be made, it is checkmate
        // All accessible squares are empty
        for (Piece piece : getGame().getPieces(otherColor)) {
            if (!piece.getAccessibleSquares().isEmpty()) {
                checkmate = false;
                break;
            }
        }
        if (checkmate) {
            getGame().checkmate();
            return true;
        }

        if (this.getColor() == ChessColor.WHITE) {
            List<Piece> clonedPieces = new ArrayList<>();
            for (Piece piece : getGame().getBlackPieces()) {
                try {
                    clonedPieces.add((Piece) piece.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            getGame().setSavedPosition(new Position(getGame().getWhitePieces(), clonedPieces));
            getGame().makeMove();
        }

        return true;
    }

    public abstract List<Square> getAccessibleSquares();

    public abstract List<Square> getInfluencedSquares();

    public abstract List<Square> getInfluencedRowSquares();
    public abstract List<Square> getInfluencedColumnSquares();
    public abstract List<Square> getInfluencedFirstDiagonalSquares();
    public abstract List<Square> getInfluencedSecondDiagonalSquares();

    // public abstract List<Field> getFields();

    public abstract String getPath();

    public void setChecking(Field checkingField) {
        this.checkingField = checkingField;
    }

    public Field getChecking() {
        return checkingField;
    }

    public int getMoves() {
        return this.moves;
    }

    // All the squares which this piece blocks other pieces from accessing
    public List<Square> getSquares() {
        List<Square> squares = new ArrayList<>();
        squares.addAll(this.getRowSquares());
        squares.addAll(this.getColumnSquares());
        squares.addAll(this.getFirstDiagonalSquares());
        squares.addAll(this.getSecondDiagonalSquares());
        return squares;
    }

    public List<Square> getRowSquares() {
        // Samtliga rutor (även de opåverkade) läggs till
        List<Square> squares = new ArrayList<>(this.getSquare().getRow().getSquares());
        // Ta bort den rutan som pjäsen står på (kan ej göra ett drag till samma ruta)
        squares.remove(this.getSquare());
        // Lista med rutor som ska tas bort innan metoden returneras (alla som är opåverkade)
        List<Square> removeSquares = new ArrayList<>();

        // Loopa igenom alla rutor (gå igenom varje ruta en och en och kolla följande: )
        for (int i = squares.size() - 1; i >= 0; i--) {

            // Slipper skriva squares.get(i) för att få den aktuella rutan (behöver endast skriva square)
            Square square = squares.get(i);

            // Kör endast ifall rutan har en pjäs
            if (square.hasPiece() && !square.getPiece().equals(this)) {

                // Kör endast ifall rutan är på samma rad som den rutan som pjäsen står på
                if (square.getRow().equals(this.getSquare().getRow())) {

                    // (1)
                    if (square.getColumn().getId() < this.getSquare().getColumn().getId()) {
                        List<Square> belowRowSquares = this.getSquare().getRow().getSquares().subList(0, this.getSquare().getColumn().getId());
                        for (int j = belowRowSquares.size() - 1; j >= 0; j--) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(0, j));
                                break;
                            }
                        }
                        // (2)
                    } else {
                        List<Square> belowRowSquares = this.getSquare().getRow().getSquares().subList(this.getSquare().getColumn().getId() + 1, this.getSquare().getColumn().getSquares().size());
                        for (int j = 0; j < belowRowSquares.size(); j++) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(j + 1, belowRowSquares.size()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        squares.removeAll(removeSquares);
        return squares;
    }

    public List<Square> getColumnSquares() {
        List<Square> squares = new ArrayList<>(this.getSquare().getColumn().getSquares());
        squares.remove(this.getSquare());
        List<Square> removeSquares = new ArrayList<>();
        for (int i = squares.size() - 1; i >= 0; i--) {
            Square square = squares.get(i);
            if (square.hasPiece() && !square.getPiece().equals(this)) {
                // Same column
                if (square.getColumn().equals(getSquare().getColumn())) {
                    // Lower row id (higher up)
                    if (square.getRow().getId() < this.getSquare().getRow().getId()) {
                        List<Square> belowRowSquares = this.getSquare().getColumn().getSquares().subList(0, this.getSquare().getRow().getId());
                        for (int j = belowRowSquares.size() - 1; j >= 0; j--) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(0, j));
                                break;
                            }
                        }
                        // Higher row id (lower down)
                    } else {
                        List<Square> belowRowSquares = this.getSquare().getColumn().getSquares().subList(this.getSquare().getRow().getId() + 1, this.getSquare().getRow().getSquares().size());
                        for (int j = 0; j < belowRowSquares.size(); j++) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(j + 1, belowRowSquares.size()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        squares.removeAll(removeSquares);
        return squares;
    }

    public List<Square> getFirstDiagonalSquares() {
        Set<Square> setSquares = new LinkedHashSet<>(this.getSquare().getFirstDiagonal().getSquares());
        setSquares.remove(this.getSquare());
        List<Square> squares = new ArrayList<>(setSquares);
        List<Square> removeSquares = new ArrayList<>();

        for (int i = squares.size() - 1; i >= 0; i--) {
            Square square = squares.get(i);
            if (square.hasPiece() && !square.getPiece().equals(this)) {

                // First diagonal
                if (square.getFirstDiagonal().equals(this.getSquare().getFirstDiagonal())) {

                    // More left
                    if (square.getColumn().getId() < this.getSquare().getColumn().getId()) {

                        // Reversed sublist from rook as the squares of a diagonal are attained in reversed order
                        List<Square> belowRowSquares = this.getSquare().getFirstDiagonal().getSquares().subList(this.getSquare().getFirstDiagonal().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getFirstDiagonal().getSquares().size());
                        for (int j = 0; j < belowRowSquares.size(); j++) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(j + 1, belowRowSquares.size()));
                                break;
                            }
                        }
                        // More right
                    } else {
                        List<Square> belowRowSquares = this.getSquare().getFirstDiagonal().getSquares().subList(0, this.getSquare().getFirstDiagonal().getSquares().indexOf(this.getSquare()));
                        for (int j = belowRowSquares.size() - 1; j >= 0; j--) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(0, j));
                                break;
                            }
                        }
                    }
                }
            }
        }
        squares.removeAll(removeSquares);

        return squares;
    }

    public List<Square> getSecondDiagonalSquares() {
        Set<Square> setSquares = new LinkedHashSet<>(this.getSquare().getSecondDiagonal().getSquares());
        setSquares.remove(this.getSquare());
        List<Square> squares = new ArrayList<>(setSquares);
        List<Square> removeSquares = new ArrayList<>();

        for (int i = squares.size() - 1; i >= 0; i--) {
            Square square = squares.get(i);
            if (square.hasPiece() && !square.getPiece().equals(this)) {

                 if (square.getSecondDiagonal().equals(this.getSquare().getSecondDiagonal())) {
                    // More left
                    if (square.getColumn().getId() < this.getSquare().getColumn().getId()) {

                        // Reversed sublist from rook as the squares of a diagonal are attained in reversed order

                        List<Square> belowRowSquares = this.getSquare().getSecondDiagonal().getSquares().subList(0, this.getSquare().getSecondDiagonal().getSquares().indexOf(this.getSquare()));
                        for (int j = belowRowSquares.size() - 1; j >= 0; j--) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(0, j));
                                break;
                            }
                        }
                        // More right
                    } else {
                        List<Square> belowRowSquares = this.getSquare().getSecondDiagonal().getSquares().subList(this.getSquare().getSecondDiagonal().getSquares().indexOf(this.getSquare()) + 1, this.getSquare().getSecondDiagonal().getSquares().size());
                        for (int j = 0; j < belowRowSquares.size(); j++) {
                            if (belowRowSquares.get(j).hasPiece()) {
                                if (belowRowSquares.get(j).getPiece() instanceof King && belowRowSquares.get(j).getPiece().getColor() != this.getColor()) continue;
                                removeSquares.addAll(belowRowSquares.subList(j + 1, belowRowSquares.size()));
                                break;
                            }
                        }
                    }
                }
            }
        }
        squares.removeAll(removeSquares);

        return squares;
    }

    public void setPosition(Point point) {
        imageHolder.setLocation(point);
    }

    public void setSquare(Square square) {
        this.square = square;

        setPosition(square.getPosition());
    }

    public JLabel getImageHolder() {
        return this.imageHolder;
    }

    public Game getGame() {
        return this.player.getGame();
    }

    public ChessColor getColor() {
        return this.player.getColor();
    }

    public Square getSquare() {
        return this.square;
    }

    public void givePriority(boolean give) {
        // Makes this piece take priority (on top of other elements)
        getGame().getBoard().setLayer(imageHolder, give ? Integer.MAX_VALUE : 0);
    }

    public String toString() {
        return getClass().getSimpleName() + " " + getColor();
    }


    /*
    Get all the squares between two pieces (a select piece and the other king)
     */

}
