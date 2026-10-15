
package com.mycompany.gui;

import com.mycompany.util.Maze;
import com.mycompany.entities.Pacman;
import com.mycompany.entities.Ghost;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Maze maze;
    private Pacman pacman;

    private int score = 0;
    private int lives = 3;

    private GhostManager ghostManager;
    private final GameRenderer renderer = new GameRenderer();

    private Timer pacmanTimer;
    private Timer mouthTimer;

    private boolean gameOver = false;
    private boolean mouthOpen = true;
    private boolean pacmanBlinking = false;
    private boolean pacmanVisible = true;

    public GamePanel() {
        maze = new Maze();
        pacman = new Pacman(1, 1);

        ghostManager = new GhostManager(
                maze, pacman, this::checkCollision, this::repaint
        );

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(570, 450));
        setFocusable(true);

        setupKeyBindings();

        pacmanTimer = new Timer(150, e -> movePacman());

        mouthTimer = new Timer(120, e -> {
            if (!pacmanBlinking && !gameOver) {
                mouthOpen = !mouthOpen;
                repaint();
            }
        });

        mouthTimer.start();
        ghostManager.start();
        pacmanTimer.start();
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
                if (!gameOver && !pacmanBlinking) {
                    pacman.setDirection(dx, dy);
                }
            }
        });
    }

    private void movePacman() {
        if (gameOver || pacmanBlinking) return;

        if (pacman.move(maze)) {
            score += 10;
        }

        checkCollision();

        if (gameOver || pacmanBlinking) return;

        if (!maze.hasDotsLeft()) {
            gameOver = true;
            stopTimers();
            JOptionPane.showMessageDialog(this, "YOU WIN!");
            closeGame();
            return;
        }

        repaint();
    }

    private void checkCollision() {
        if (gameOver || pacmanBlinking || ghostManager == null) return;

        Ghost blinky = ghostManager.getBlinky();
        Ghost pinky = ghostManager.getPinky();
        Ghost inky = ghostManager.getInky();
        Ghost clyde = ghostManager.getClyde();

        boolean hit =
                samePosition(blinky)
                || (ghostManager.isPinkyReleased() && samePosition(pinky))
                || (ghostManager.isInkyReleased() && samePosition(inky))
                || (ghostManager.isClydeReleased() && samePosition(clyde));

        if (!hit) return;

        stopTimers();
        lives--;

        if (lives <= 0) {
            gameOver = true;
            pacmanVisible = true;
            repaint();
            JOptionPane.showMessageDialog(this, "GAME OVER!");
            closeGame();
            return;
        }

        pacmanBlinking = true;
        pacmanVisible = false;

        Timer blinkTimer = new Timer(150, e -> {
            pacmanVisible = !pacmanVisible;
            repaint();
        });
        blinkTimer.start();

        Timer pauseTimer = new Timer(1200, e -> {
            blinkTimer.stop();

            pacmanBlinking = false;
            pacmanVisible = true;
            pacman = new Pacman(1, 1);

            ghostManager.setPacman(pacman);
            ghostManager.resetPositions();

            repaint();

            JOptionPane.showMessageDialog(
                    this, "You were caught! Lives left: " + lives
            );

            if (!gameOver) {
                mouthTimer.start();
                pacmanTimer.start();
                ghostManager.start();
            }
        });

        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }

    private boolean samePosition(Ghost ghost) {
        return ghost.getX() == pacman.getX()
                && ghost.getY() == pacman.getY();
    }

    private void stopTimers() {
        if (pacmanTimer != null) pacmanTimer.stop();
        if (mouthTimer != null) mouthTimer.stop();

        if (ghostManager != null) {
            ghostManager.stop();
        }
    }

    private void closeGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderer.draw(g, maze, pacman,
                ghostManager.getBlinky(),
                ghostManager.getPinky(),
                ghostManager.getInky(),
                ghostManager.getClyde(),
                score, lives, mouthOpen, pacmanVisible);
    }
}
