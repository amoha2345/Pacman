/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import javax.swing.*;
import java.awt.*;
import com.mycompany.entities.Ghost;
import com.mycompany.entities.BlinkyAI;
import javax.swing.Timer;

public class GamePanel extends JPanel {

    private Maze maze;
    private Pacman pacman;
    private int score = 0;
    private int lives = 3;
    private Ghost blinky;
    private BlinkyAI blinkyAI;
    private Timer ghostTimer;
    
    
    public GamePanel() {
        maze = new Maze();
        pacman = new Pacman(1, 1);
        blinky = new Ghost(9, 7, Color.RED);
        blinkyAI = new BlinkyAI();

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(570, 450));
        setFocusable(true);
        setupKeyBindings();

        ghostTimer = new Timer(300, e -> moveBlinky());
        ghostTimer.start();
    }

    private void setupKeyBindings() {
    bindMove("UP", "moveUp", 0, -1);
    bindMove("DOWN", "moveDown", 0, 1);
    bindMove("LEFT", "moveLeft", -1, 0);
    bindMove("RIGHT", "moveRight", 1, 0);
}
    private void bindMove(String key, String name, int dx, int dy) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(key), name);
        getActionMap().put(name, new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int newX = pacman.getX() + dx;
            int newY = pacman.getY() + dy;
            if (newX >= 0 && newX < maze.getCols()
                    && newY >= 0 && newY < maze.getRows()
                    && maze.hasDot(newX, newY)
                    && !maze.isWall(newX, newY)) {
                score += 10;
            }
            pacman.move(dx, dy, maze);
            if (!maze.hasDotsLeft()) {
                JOptionPane.showMessageDialog(GamePanel.this, "YOU WIN!");
            }
            repaint();
        }
        });
}  
    private void moveBlinky() {
        int[] move = blinkyAI.getNextMove(blinky, pacman, maze);

        blinky.moveTo(
            blinky.getX() + move[0],
            blinky.getY() + move[1]
        );

        checkCollision();
        repaint();
    }

    private void checkCollision() {
        if (blinky.getX() == pacman.getX()
                && blinky.getY() == pacman.getY()) {

            lives--;

            if (lives <= 0) {
                ghostTimer.stop();
                JOptionPane.showMessageDialog(this, "GAME OVER!");
            } else {
                pacman = new Pacman(1, 1);
                blinky.moveTo(9, 7);

                JOptionPane.showMessageDialog(
                    this, "You were caught! Lives left: " + lives
                );
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = 30;

        for (int y = 0; y < maze.getRows(); y++) {
            for (int x = 0; x < maze.getCols(); x++) {

                int px = x * tileSize;
                int py = y * tileSize;

                if (maze.isWall(x, y)) {
                    g.setColor(Color.BLUE);
                    g.fillRect(px, py, tileSize, tileSize);
                } else if (maze.hasDot(x, y)) {
                    g.setColor(Color.WHITE);
                    g.fillOval(px + 12, py + 12, 6, 6);
                }
            }
        }


        // Draw Pac-Man
        g.setColor(Color.YELLOW);
        g.fillOval(
            pacman.getX() * tileSize + 3,
            pacman.getY() * tileSize + 3,
            24, 24
        );

        // Draw Blinky
        g.setColor(blinky.getColor());
        g.fillOval(
            blinky.getX() * tileSize + 3,
            blinky.getY() * tileSize + 3,
            24, 24
        );

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 445);
        g.drawString("Lives: " + lives, 150, 445);
    }
}