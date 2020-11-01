package com.company.assets.gui;

import com.company.assets.ChessColor;
import com.company.assets.exceptions.PlayerColorException;
import com.company.assets.game.Game;
import com.company.assets.game.Player;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private Board board = new Board(this);
    private JDialog panel = new JDialog();
    private JLayeredPane layeredPane = new JLayeredPane();

    private Frame() {
        init();
    }

    private void init() {

        Container pane = getContentPane();
        pane.setMinimumSize(new Dimension(800, 800));
        pane.setPreferredSize(new Dimension(800, 800));
        pane.setMaximumSize(new Dimension(800, 800));

        pane.add(layeredPane, BorderLayout.CENTER);
        board.setBounds(0, 0, 800, 800);
        board.setOpaque(true);

        panel.setMinimumSize(new Dimension(800, 800));
        panel.setPreferredSize(new Dimension(800, 800));
        panel.setMaximumSize(new Dimension(800, 800));
        panel.setLocationRelativeTo(null);

        layeredPane.add(board, 0, 0);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }


    public static void main(String[] args) {
        new Frame();
    }

}
